package com.rexvit.rexlogger

import android.content.Context
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

class FileLogWriter(
    private val context: Context,
    private val logFileName: String = "app_logs.log",
    private val maxLogFiles: Int = 5,
    private val rotationStrategy: LogRotationStrategy = SizeBasedRotation(1024 * 1024) // 1MB
) {
    private val logFile: File by lazy {
        File(context.filesDir, logFileName).apply {
            if (!exists()) {
                parentFile?.mkdirs()
                createNewFile()
            }
        }
    }

    @Synchronized
    fun writeLog(message: String) {
        try {
            if (rotationStrategy.shouldRotate(logFile)) {
                rotationStrategy.rotate(logFile)
                cleanupOldLogs()
            }

            FileUtils.writeStringToFile(logFile, "$message\n", Charsets.UTF_8, true)
        } catch (e: IOException) {
            // Fallback to system log if file writing fails
            android.util.Log.e("FileLogWriter", "Failed to write log to file", e)
        }
    }

    fun getLogs(): List<String> {
        return try {
            if (logFile.exists()) {
                FileUtils.readLines(logFile, Charsets.UTF_8)
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            android.util.Log.e("FileLogWriter", "Failed to read logs", e)
            emptyList()
        }
    }

    fun clearLogs() {
        try {
            if (logFile.exists()) {
                FileUtils.write(logFile, "", Charsets.UTF_8)
            }
        } catch (e: IOException) {
            android.util.Log.e("FileLogWriter", "Failed to clear logs", e)
        }
    }

    private fun cleanupOldLogs() {
        val logDir = logFile.parentFile ?: return
        val logFiles = logDir.listFiles { file ->
            file.name.startsWith(logFile.nameWithoutExtension) && file.name.endsWith(logFile.extension)
        }?.sortedBy { it.lastModified() } ?: return

        if (logFiles.size > maxLogFiles) {
            logFiles.take(logFiles.size - maxLogFiles).forEach { it.delete() }
        }
    }
}