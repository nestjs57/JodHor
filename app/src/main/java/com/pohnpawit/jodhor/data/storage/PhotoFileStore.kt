package com.pohnpawit.jodhor.data.storage

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoFileStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val photosDir: File by lazy {
        File(context.filesDir, "photos").apply { if (!exists()) mkdirs() }
    }
    private val coversDir: File by lazy {
        File(context.filesDir, "covers").apply { if (!exists()) mkdirs() }
    }

    fun createPhotoFile(): File =
        File(photosDir, "photo_${System.currentTimeMillis()}.jpg")

    fun createCoverFile(): File =
        File(coversDir, "cover_${System.currentTimeMillis()}.jpg")

    fun uriFor(file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    suspend fun copyFromUri(uri: Uri): String? = copyToFile(uri, createPhotoFile())

    suspend fun copyCoverFromUri(uri: Uri): String? = copyToFile(uri, createCoverFile())

    private suspend fun copyToFile(uri: Uri, file: File): String? = withContext(Dispatchers.IO) {
        val stream = context.contentResolver.openInputStream(uri)
        if (stream == null) {
            file.delete()
            return@withContext null
        }
        stream.use { input -> file.outputStream().use(input::copyTo) }
        file.absolutePath
    }

    suspend fun delete(path: String) = withContext(Dispatchers.IO) {
        runCatching { File(path).takeIf { it.exists() }?.delete() }
    }
}
