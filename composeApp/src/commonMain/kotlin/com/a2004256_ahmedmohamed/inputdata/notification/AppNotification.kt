package com.a2004256_ahmedmohamed.inputdata.notification

import kotlin.random.Random
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

data class AppNotification @OptIn(ExperimentalTime::class) constructor(
    val id: String = generateId(),
    val title: String = "",
    val message: String = "",
    val timestamp: Long = now().toEpochMilliseconds()
)

fun generateId(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..16)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
}