package com.rexvit.rexlogger

import java.text.SimpleDateFormat
import java.util.*

interface LogFormatter {
    fun format(level: LogLevel, tag: String, message: String, throwable: Throwable?): String

    companion object {
        val DEFAULT: LogFormatter = object : LogFormatter {
            private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

            override fun format(level: LogLevel, tag: String, message: String, throwable: Throwable?): String {
                val timestamp = dateFormat.format(Date())
                val throwableStr = throwable?.let {
                    "\n${it.stackTraceToString()}"
                } ?: ""
                return "$timestamp [${level.name}] $tag: $message$throwableStr"
            }
        }
    }
}