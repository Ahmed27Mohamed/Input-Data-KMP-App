package com.a2004256_ahmedmohamed.inputdata

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import java.io.ByteArrayOutputStream

lateinit var appContext: Context

@OptIn(ExperimentalEncodingApi::class)
actual suspend fun encodeImageToBase64(uri: String): String = withContext(Dispatchers.IO) {
    try {
        val inputStream = appContext.contentResolver.openInputStream(Uri.parse(uri)) ?: return@withContext ""

        // 1. قراءة أبعاد الصورة دون تحميلها في الذاكرة
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream.close()

        // 2. حساب نسبة الضغط المناسبة
        val sampleSize = calculateSampleSize(options.outWidth, options.outHeight)

        // 3. تحميل الصورة مضغوطة
        val inputStream2 = appContext.contentResolver.openInputStream(Uri.parse(uri)) ?: return@withContext ""
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        }
        val bitmap = BitmapFactory.decodeStream(inputStream2, null, decodeOptions)
        inputStream2.close()

        // 4. ضغط الجودة وإضافة البادئة الصحيحة
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, outputStream) // 70% جودة
        val byteArray = outputStream.toByteArray()

        "data:image/jpeg;base64,${Base64.encode(byteArray)}"
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

private fun calculateSampleSize(width: Int, height: Int, reqWidth: Int = 1024, reqHeight: Int = 1024): Int {
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}