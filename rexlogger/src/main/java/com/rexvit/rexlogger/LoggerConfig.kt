package com.rexvit.rexlogger

import android.content.Context

data class LoggerConfig(
    val minLogLevel: LogLevel = LogLevel.DEBUG,
    val isFileLoggingEnabled: Boolean = false,
    val fileLoggingConfig: FileLoggingConfig? = null,
    val formatter: LogFormatter = LogFormatter.DEFAULT,
    val isLogcatEnabled: Boolean = true
) {
    data class FileLoggingConfig(
        val context: Context,
        val logFileName: String = "app_logs.log",
        val maxLogFiles: Int = 5,
        val rotationStrategy: LogRotationStrategy = SizeBasedRotation(1024 * 1024) // 1MB
    )
}