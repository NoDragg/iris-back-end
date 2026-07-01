package com.iris.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

@Service
class CloudinaryService(
    @Value("\${cloudinary.cloud-name}") private val cloudName: String,
    @Value("\${cloudinary.api-key}") private val apiKey: String,
    @Value("\${cloudinary.api-secret}") private val apiSecret: String,
    @Value("\${cloudinary.upload.avatar-folder:avatars}") private val avatarFolder: String,
    @Value("\${cloudinary.upload.vod-folder:vods}") private val vodFolder: String
) {
    private val uploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/auto/upload"

    fun uploadAvatar(file: MultipartFile): String {
        val params = mapOf(
            "folder" to avatarFolder,
            "transformation" to "c_fill,w_200,h_200,g_face"
        )
        return upload(file, "image", params)
    }

    fun uploadVOD(file: MultipartFile): String {
        val params = mapOf("folder" to vodFolder)
        return upload(file, "video", params)
    }

    private fun upload(file: MultipartFile, type: String, params: Map<String, String>): String {
        val boundary = UUID.randomUUID().toString()
        val url = URL("$uploadUrl?resource_type=$type")

        val conn = url.openConnection() as HttpURLConnection
        conn.doOutput = true
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

        val os = conn.outputStream
        val writer = os.bufferedWriter()

        // File part
        writer.write("--$boundary\r\n")
        writer.write("Content-Disposition: form-data; name=\"file\"; filename=\"${file.originalFilename}\"\r\n")
        writer.write("Content-Type: ${file.contentType ?: "application/octet-stream"}\r\n\r\n")
        writer.flush()
        os.write(file.bytes)
        os.flush()

        // API Key
        writer.write("\r\n--$boundary\r\n")
        writer.write("Content-Disposition: form-data; name=\"api_key\"\r\n\r\n")
        writer.write("$apiKey\r\n")
        writer.flush()

        // Add params
        params.forEach { (k, v) ->
            writer.write("--$boundary\r\n")
            writer.write("Content-Disposition: form-data; name=\"$k\"\r\n\r\n")
            writer.write("$v\r\n")
            writer.flush()
        }

        writer.write("--$boundary--\r\n")
        writer.close()

        // Read response
        val response = conn.inputStream.bufferedReader().readText()
        conn.disconnect()

        // Parse JSON manually - find "secure_url"
        val urlMatch = Regex("\"secure_url\"\\s*:\\s*\"([^\"]+)\"").find(response)
        return urlMatch?.groupValues?.get(1) ?: throw RuntimeException("Upload failed: $response")
    }
}
