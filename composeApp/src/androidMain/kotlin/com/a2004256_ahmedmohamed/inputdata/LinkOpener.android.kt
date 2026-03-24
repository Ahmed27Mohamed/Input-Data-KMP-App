package com.a2004256_ahmedmohamed.inputdata

import android.content.Intent
import android.net.Uri

actual fun openLink(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    getContext().startActivity(intent)
}