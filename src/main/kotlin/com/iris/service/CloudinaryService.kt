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
    // Sử dụng unsigned upload - cần tạo upload preset trên Cloudinary Dashboard
    private val uploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"
    private val videoUploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/video/upload"

    fun uploadAvatar(file: MultipartFile): String {
        val tempFile = multipartToFile(file)
        return try {
            uploadToCloudinary(tempFile, uploadUrl)
        } finally {
            tempFile.delete()
        }
    }

    fun uploadVOD(file: MultipartFile): String {
        val tempFile = multipartToFile(file)
        return try {
            uploadToCloudinary(tempFile, videoUploadUrl)
        } finally {
            tempFile.delete()
        }
    }

    private fun uploadToCloudinary(file: File, uploadEndpoint: String): String {
        val boundary = UUID.randomUUID().toString()
        val url = URL("$uploadEndpoint?upload_preset=ml_default")

        val conn = url.openConnection() as HttpURLConnection
        conn.doOutput = true
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

        val os = conn.outputStream
        val writer = os.bufferedWriter()

        // File part
        writer.write("--$boundary\r\n")
        writer.write("Content-Disposition: form-data; name=\"file\"; filename=\"${file.name}\"\r\n")
        writer.write("Content-Type: application/octet-stream\r\n\r\n")
        writer.flush()
        os.write(file.readBytes())
        os.flush()

        // Upload preset (unsigned)
        writer.write("\r\n--$boundary\r\n")
        writer.write("Content-Disposition: form-data; name=\"upload_preset\"\r\n\r\n")
        writer.write("ml_default\r\n")
        writer.flush()

        writer.write("--$boundary--\r\n")
        writer.close()

        // Read response
        val response = conn.inputStream.bufferedReader().readText()
        conn.disconnect()

        // Parse JSON response - tìm secure_url
        val pattern = Regex(""""secure_url"\s*:\s*"([^"]+)""")
        val urlMatch = pattern.find(response)
        return urlMatch?.groupValues?.get(1) ?: throw RuntimeException("Upload failed: $response")
    }

    private fun multipartToFile(file: MultipartFile): File {
        val tempFile = File.createTempFile("upload_", file.originalFilename ?: "file")
        FileOutputStream(tempFile).use { it.write(file.bytes) }
        return tempFile
    }
}
