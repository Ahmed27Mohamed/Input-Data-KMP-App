package com.a2004256_ahmedmohamed.inputdata

import kotlinx.browser.window

fun requestWebNotificationPermission() {
    val win = window.asDynamic()
    if (win.Notification == undefined) {
        window.alert("Web notifications not supported")
        return
    }

    when (win.Notification.permission as String) {
        "granted" -> window.alert("Notifications already granted ✅")
        "default" -> {
            win.Notification.requestPermission { permission: String ->
                if (permission == "granted") {
                    window.alert("Notifications granted ✅")
                } else {
                    window.alert("Notifications denied ❌")
                }
            }
        }
        "denied" -> window.alert("Notifications blocked ❌")
    }
}