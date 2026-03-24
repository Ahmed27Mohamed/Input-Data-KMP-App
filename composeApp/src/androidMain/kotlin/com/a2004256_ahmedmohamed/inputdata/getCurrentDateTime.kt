package com.a2004256_ahmedmohamed.inputdata

import java.text.SimpleDateFormat
import java.util.*

actual fun getCurrentDateTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(Date())
}