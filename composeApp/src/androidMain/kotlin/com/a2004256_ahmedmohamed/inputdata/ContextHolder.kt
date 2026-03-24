package com.a2004256_ahmedmohamed.inputdata

import android.content.Context

private lateinit var applicationContext: Context

fun initContext(context: Context) {
    applicationContext = context
}

fun getContext(): Context = applicationContext