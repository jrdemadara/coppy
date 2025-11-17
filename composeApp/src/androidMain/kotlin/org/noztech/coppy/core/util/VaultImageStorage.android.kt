package org.noztech.coppy.core.util

import android.content.Context
import okio.buffer
import okio.sink
import java.io.File

actual class VaultImageStorage(private val context: Context) {
    private val vaultDir = File(context.filesDir, "vault_images").apply { mkdirs() }

    actual suspend fun saveImage(bytes: ByteArray, filename: String): String {
        val file = File(vaultDir, filename)
        file.sink().buffer().use { it.write(bytes) }
        return file.absolutePath
    }

    actual suspend fun deleteImage(path: String) {
        File(path).delete()
    }

    actual suspend fun getImagePath(filename: String): String {
        return File(vaultDir, filename).absolutePath
    }
}