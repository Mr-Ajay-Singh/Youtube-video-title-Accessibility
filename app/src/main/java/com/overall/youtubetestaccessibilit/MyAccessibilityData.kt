package com.overall.youtubetestaccessibilit

import androidx.annotation.Keep

@Keep
data class MyAccessibilityEvent(
    val eventType: Int = 0,
    val contentChangeTypes: Int = 0,
    val packageName: String = "",
    val className: String = "",
    val text: String = "",
    val contentDescription: String = ""
)