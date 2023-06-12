package com.onpu.app.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

fun Uri.deleteLastEntityInSlashSplit() : Uri{
    this.toString().split("/").let{
        return this.toString().replace(it[it.size-1], "").toUri()
    }
}

fun String.deleteLastEntityInSlashSplit() : String{
    this.split("/").let{
        return this.replace(it[it.size-1], "")
    }
}

fun File.readFile(): String {
    if(!this.exists()) {
        this.createNewFile()
        return ""
    }
    return this.bufferedReader().use { it.readText() }
}

fun getImageContentUri(context: Context, file: File): Uri? {
    val filePath = file.absolutePath
    val cursor: Cursor? = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID),
        MediaStore.Images.Media.DATA + "=? ", arrayOf(filePath), null
    )
    return if (cursor != null && cursor.moveToFirst()) {
        val id: Int = cursor.getInt(
            cursor.getColumnIndex(MediaStore.MediaColumns._ID)
        )
        val baseUri = Uri.parse("content://media/external/images/media")
        Uri.withAppendedPath(baseUri, "" + id)
    } else {
        if (file.exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, filePath)
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )
        } else {
            null
        }
    }
}

fun<T> File.outputStream(use: File.(OutputStream) -> T?) = outputStream().use { use(this, it) }
fun<T> File.inputStream(use: File.(InputStream) -> T?) = inputStream().use { use(this, it) }

fun<T> File.zipOutputStream(use: File.(ZipOutputStream) -> T?) = ZipOutputStream(outputStream())
    .use { use(this, it) }
fun<T> File.zipInputStream(use: File.(ZipInputStream) -> T?) = ZipInputStream(inputStream())
    .use { use(this, it) }

fun OutputStream.write(byte: ByteArray, off: Int, len: Int, function: (len: Int) -> Unit) {
    write(byte, off, len)
    function(len)
}

fun OutputStream.write(byte: ByteArray, len: Int, function: (len: Int) -> Unit) {
    write(byte, len)
    function(len)
}

fun OutputStream.write(byte: ByteArray, len: Int) = write(byte, 0, len)

fun getImageFileFromUri(context: Context, uri: Uri): File {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    var filePath = ""
    cursor?.run {
        moveToFirst()
        val index = getColumnIndex(MediaStore.Images.Media.DATA)
        filePath = getString(index)
        close()
    }
    return File(filePath)
}