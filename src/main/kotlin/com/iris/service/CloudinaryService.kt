package com.iris.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.util.UUID

@Service
class CloudinaryService(
    @Value("\${cloudinary.cloud-name}") private val cloudName: String,
    @Value("\${cloudinary.api-key}") private val apiKey: String,
    @Value("\${cloudinary.api-secret}") private val apiSecret: String,
    @Value("\${cloudinary.upload.avatar-folder:avatars}") private val avatarFolder: String,
    @Value("\${cloudinary.upload.vod-folder:vods}") private val vodFolder: String
) {
    // Signed upload - dùng api-secret, không cần upload preset trên Dashboard
    private val uploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"
    private val videoUploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/video/upload"

    fun uploadAvatar(file: MultipartFile): String {
        val tempFile = multipartToFile(file)
        return try {
            uploadToCloudinary(tempFile, uploadUrl, avatarFolder)
        } finally {
            tempFile.delete()
        }
    }

    fun uploadVOD(file: MultipartFile): String {
        val tempFile = multipartToFile(file)
        return try {
            uploadToCloudinary(tempFile, videoUploadUrl, vodFolder)
        } finally {
            tempFile.delete()
        }
    }

    private fun uploadToCloudinary(file: File, uploadEndpoint: String, folder: String): String {
        val boundary = UUID.randomUUID().toString()
        val timestamp = (System.currentTimeMillis() / 1000).toString()

        // Signature: sha1 của các param (sort theo key) + api_secret
        val paramsToSign = "folder=$folder&timestamp=$timestamp"
        val signature = sha1(paramsToSign + apiSecret)

        val conn = URL(uploadEndpoint).openConnection() as HttpURLConnection
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

        // Signed params
        writeField(writer, boundary, "api_key", apiKey)
        writeField(writer, boundary, "timestamp", timestamp)
        writeField(writer, boundary, "folder", folder)
        writeField(writer, boundary, "signature", signature)

        writer.write("--$boundary--\r\n")
        writer.close()

        val status = conn.responseCode
        val response = if (status in 200..299) {
            conn.inputStream.bufferedReader().readText()
        } else {
            val errBody = conn.errorStream?.bufferedReader()?.readText() ?: ""
            conn.disconnect()
            throw RuntimeException("Cloudinary upload failed ($status): $errBody")
        }
        conn.disconnect()

        val pattern = Regex(""""secure_url"\s*:\s*"([^"]+)""")
        val urlMatch = pattern.find(response)
        return urlMatch?.groupValues?.get(1) ?: throw RuntimeException("Upload failed: $response")
    }

    private fun writeField(writer: java.io.BufferedWriter, boundary: String, name: String, value: String) {
        writer.write("\r\n--$boundary\r\n")
        writer.write("Content-Disposition: form-data; name=\"$name\"\r\n\r\n")
        writer.write(value)
        writer.flush()
    }

    private fun sha1(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun multipartToFile(file: MultipartFile): File {
        val tempFile = File.createTempFile("upload_", file.originalFilename ?: "file")
        FileOutputStream(tempFile).use { it.write(file.bytes) }
        return tempFile
    }
}
