package com.a2004256_ahmedmohamed.inputdata

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Input Data",
    ) {
        App()
    }
}