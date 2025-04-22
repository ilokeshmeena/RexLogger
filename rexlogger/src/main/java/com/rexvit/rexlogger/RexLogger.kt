package com.rexvit.rexlogger

import android.util.Log

object EnhancedLogger {
    private var config: LoggerConfig = LoggerConfig()
    private var fileLogWriter: FileLogWriter? = null

    fun initialize(config: LoggerConfig) {
        this.config = config

        if (config.isFileLoggingEnabled && config.fileLoggingConfig != null) {
            fileLogWriter = FileLogWriter(
                context = config.fileLoggingConfig.context,
                logFileName = config.fileLoggingConfig.logFileName,
                maxLogFiles = config.fileLoggingConfig.maxLogFiles,
                rotationStrategy = config.fileLoggingConfig.rotationStrategy
            )
        } else {
            fileLogWriter = null
        }
    }

    fun v(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.VERBOSE, tag, message, throwable)
    }

    fun d(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.DEBUG, tag, message, throwable)
    }

    fun i(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.INFO, tag, message, throwable)
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.WARNING, tag, message, throwable)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.ERROR, tag, message, throwable)
    }

    fun c(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.CRITICAL, tag, message, throwable)
    }

    fun getLogs(): List<String> {
        return fileLogWriter?.getLogs() ?: emptyList()
    }

    fun clearLogs() {
        fileLogWriter?.clearLogs()
    }

    private fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        if (level.value < config.minLogLevel.value) return

        val formattedMessage = config.formatter.format(level, tag, message, throwable)

        if (config.isLogcatEnabled) {
            when (level) {
                LogLevel.VERBOSE -> Log.v(tag, message, throwable)
                LogLevel.DEBUG -> Log.d(tag, message, throwable)
                LogLevel.INFO -> Log.i(tag, message, throwable)
                LogLevel.WARNING -> Log.w(tag, message, throwable)
                LogLevel.ERROR, LogLevel.CRITICAL -> Log.e(tag, message, throwable)
                else -> {}
            }
        }

        if (config.isFileLoggingEnabled) {
            fileLogWriter?.writeLog(formattedMessage)
        }
    }
}