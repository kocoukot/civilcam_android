package com.civilcam.utils

import android.util.Log
import java.util.logging.*


class AndroidLoggingHandler : Handler() {
    override fun close() {}
    override fun flush() {}
    override fun publish(record: LogRecord) {
        if (!super.isLoggable(record)) return
        val name = record.loggerName
        val maxLength = 30
        val tag = if (name.length > maxLength) name.substring(name.length - maxLength) else name
        try {
            val level = getAndroidLevel(record.level)
            Log.println(level, tag, record.message)
            if (record.thrown != null) {
                Log.println(level, tag, Log.getStackTraceString(record.thrown))
            }
        } catch (e: RuntimeException) {
            Log.e("AndroidLoggingHandler", "Error logging message.", e)
        }
    }

    companion object {
        fun reset(rootHandler: Handler) {
            val rootLogger: Logger = LogManager.getLogManager().getLogger("")
            val handlers: Array<Handler> = rootLogger.handlers
            for (handler in handlers) {
                rootLogger.removeHandler(handler)
            }
            rootLogger.addHandler(rootHandler)
        }

        fun getAndroidLevel(level: Level): Int {
            val value = level.intValue()
            if (value >= Level.SEVERE.intValue()) return Log.ERROR
            if (value >= Level.WARNING.intValue()) return Log.WARN
            if (value >= Level.INFO.intValue()) return Log.INFO
            return Log.DEBUG
        }
    }
}