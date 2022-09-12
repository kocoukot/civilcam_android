package com.civilcam.utils

import android.content.Context
import android.net.Uri
import com.civilcam.BuildConfig
import com.civilcam.ext_features.Constant
import java.io.*


object FileUtils {

    fun createFileFromUri(context: Context, fileName: String, uri: Uri): File? {
        return context.contentResolver.openInputStream(uri)
            ?.use { inputStream ->
                val (name, extension) = getFileNameWithExtension(fileName)
                val tempFile = File.createTempFile(name, extension)
                    .apply { deleteOnExit() }
                runCatching { FileOutputStream(tempFile) }
                    .onSuccess { copy(inputStream, it) }
                tempFile
            }
    }

    fun createFileFromResource(context: Context, resourceName: String): File? {
        val uri =
            Uri.parse("android.resource://${BuildConfig.APPLICATION_ID}/drawable/${resourceName}")
        return context.contentResolver.openInputStream(uri)
            ?.use { inputStream ->
                val tempFile =
                    File.createTempFile(resourceName, ".${Constant.DEFAULT_PICTURE_EXTENSION}")
                        .apply { deleteOnExit() }
                runCatching { FileOutputStream(tempFile) }
                    .onSuccess { copy(inputStream, it) }
                tempFile
            }
    }


    private fun getFileNameWithExtension(fileName: String): Pair<String, String> {
        var extension = ""
        var name = fileName
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }

        return name to extension
    }

    @Throws(IOException::class)
    private fun copy(input: InputStream, output: OutputStream): Long {
        var n: Int
        var count: Long = 0
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (input.read(buffer).also { n = it } != -1) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }
}