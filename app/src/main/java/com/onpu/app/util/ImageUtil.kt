package com.onpu.app.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import java.io.File

fun Bitmap.inString(format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): String {
    val byteArrayBitmapStream = ByteArrayOutputStream()
    compress(format, 100, byteArrayBitmapStream)
    return Base64.encodeToString(byteArrayBitmapStream.toByteArray(), Base64.DEFAULT).replace("\n", "")

}

fun Bitmap.toFile(pathname: String): File {
    val file = File(pathname)
    val outputStream = file.outputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return file
}


inline val Drawable.drawableToString: String
    get() {
        val byteArrayBitmapStream = ByteArrayOutputStream()
        toBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream)
        return Base64.encodeToString(byteArrayBitmapStream.toByteArray(), Base64.DEFAULT)
    }

inline val Drawable.toBitmap: Bitmap
    get() = (this as BitmapDrawable).toBitmap()

fun Activity.write(bitmap: Bitmap) = File(filesDir, "${System.currentTimeMillis()}.jpg").apply {
    if (exists()) delete()
    outputStream {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, it)
        it.flush()
        it.close()
    }
}
