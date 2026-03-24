package com.a2004256_ahmedmohamed.inputdata

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

@SuppressLint("ServiceCast")
actual fun copyToClipboard(text: String) {
    val ctx = getContext()
    val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}