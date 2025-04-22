package com.rexvit.rexlogger

import java.io.File

interface LogRotationStrategy {
    fun shouldRotate(logFile: File): Boolean
    fun rotate(logFile: File): File
}

class SizeBasedRotation(private val maxSizeBytes: Long) : LogRotationStrategy {
    override fun shouldRotate(logFile: File): Boolean {
        return logFile.exists() && logFile.length() >= maxSizeBytes
    }

    override fun rotate(logFile: File): File {
        if (!logFile.exists()) return logFile

        val timestamp = System.currentTimeMillis()
        val rotatedFile = File(logFile.parent, "${logFile.nameWithoutExtension}_$timestamp.${logFile.extension}")

        if (logFile.renameTo(rotatedFile)) {
            return logFile.apply { createNewFile() }
        }
        return logFile
    }
}

class TimeBasedRotation(private val rotationIntervalMillis: Long) : LogRotationStrategy {
    private var lastRotationTime = 0L

    override fun shouldRotate(logFile: File): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime - lastRotationTime >= rotationIntervalMillis
    }

    override fun rotate(logFile: File): File {
        lastRotationTime = System.currentTimeMillis()
        return SizeBasedRotation(Long.MAX_VALUE).rotate(logFile) // Just rename with timestamp
    }
}