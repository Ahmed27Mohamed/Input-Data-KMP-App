package com.a2004256_ahmedmohamed.inputdata

import kotlin.js.Date

actual fun getCurrentDateTime(): String {
    return js("new Date().toISOString()") as String
}