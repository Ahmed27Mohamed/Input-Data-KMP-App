package com.a2004256_ahmedmohamed.inputdata

import android.app.Activity
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

lateinit var activityProvider: () -> Activity

actual suspend fun pickImageFile(): String? = suspendCancellableCoroutine { cont ->
    val activity = activityProvider()

    val requestCode = 999

    val callback = object : ActivityResultCallback {
        override fun onImagePicked(uri: Uri?) {
            cont.resume(uri?.toString())
            ActivityResultHolder.callback = null
        }
    }

    ActivityResultHolder.callback = callback

    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "image/*"
    }

    activity.startActivityForResult(intent, requestCode)
}