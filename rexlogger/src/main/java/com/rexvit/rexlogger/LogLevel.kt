package com.rexvit.rexlogger

enum class LogLevel(val value: Int) {
    VERBOSE(0),
    DEBUG(1),
    INFO(2),
    WARNING(3),
    ERROR(4),
    CRITICAL(5),
    NONE(6);

    companion object {
        fun fromInt(value: Int): LogLevel {
            return entries.firstOrNull { it.value == value } ?: NONE
        }
    }
}