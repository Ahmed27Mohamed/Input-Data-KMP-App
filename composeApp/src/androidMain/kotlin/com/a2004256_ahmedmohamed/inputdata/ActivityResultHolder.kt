package com.a2004256_ahmedmohamed.inputdata

import android.net.Uri

interface ActivityResultCallback {
    fun onImagePicked(uri: Uri?)
}

object ActivityResultHolder {
    var callback: ActivityResultCallback? = null
}