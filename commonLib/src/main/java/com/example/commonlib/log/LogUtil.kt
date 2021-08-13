package com.example.commonlib.log

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.android.LogcatAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.util.FileSize
import org.slf4j.LoggerFactory

object LogUtil {
    private var logger: Logger? = null
    enum class Level {
        VERBOSE, DEBUG, INFO, WARN, ERROR, FATAL
    }

    fun initLogger(logPath:String,level: Level){
        if (logger != null){
            return
        }
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.stop()

        val rollingFileAppender = RollingFileAppender<ILoggingEvent>()
        rollingFileAppender.isAppend = true
        rollingFileAppender.context = loggerContext

        val rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>()
        rollingPolicy.fileNamePattern = "$logPath/Learning-log-%d{yyyy-MM-dd}.log"
        rollingPolicy.setParent(rollingFileAppender)
        rollingPolicy.context = loggerContext
        rollingPolicy.maxHistory = 10
        rollingPolicy.setTotalSizeCap(FileSize.valueOf("20 mb"))
        rollingPolicy.start()

        rollingFileAppender.rollingPolicy = rollingPolicy

        val fileEncoder = PatternLayoutEncoder()
        fileEncoder.pattern = "%d|%.-1level|%thread|%msg%n"
        fileEncoder.context = loggerContext
        fileEncoder.start()
        rollingFileAppender.encoder = fileEncoder
        rollingFileAppender.start()

        val logcatEncoder = PatternLayoutEncoder()
        logcatEncoder.context = loggerContext
        logcatEncoder.pattern = "LEARNING_LOG|%thread|%msg%n"
        logcatEncoder.start()

        val logcatAppender = LogcatAppender()
        logcatAppender.context = loggerContext
        logcatAppender.encoder = logcatEncoder
        logcatAppender.start()

        val logger: Logger = LoggerFactory.getLogger("MeetingSdk") as Logger
        logger.addAppender(rollingFileAppender)
        logger.addAppender(logcatAppender)
        logger.level = transLevel(level)
        this.logger = logger
    }

    fun v(tag: String, msg: String){
        logger?.trace("[$tag] $msg")
    }

    fun d(tag: String, msg: String){
        logger?.debug("[$tag] $msg")
    }
    fun i(tag: String, msg: String) {
        logger?.info("[$tag] $msg")
    }

    fun w(tag: String, msg: String) {
        logger?.warn("[$tag] $msg")
    }

    fun e(tag: String, msg: String) {
        logger?.error("[$tag] $msg")
    }

    fun e(tag: String, msg: String, tr: Throwable?) {
        logger?.error("[$tag] $msg", tr)
    }

    fun f(tag: String, msg: String, tr: Throwable?) {
        logger?.error("[$tag] $msg", tr)
    }

    fun log(level: Level, tag: String, msg: String, tr: Throwable? = null) {
        when (level) {
            Level.VERBOSE -> v(tag, msg)
            Level.DEBUG -> d(tag, msg)
            Level.INFO -> i(tag, msg)
            Level.WARN -> w(tag, msg)
            Level.ERROR -> e(tag, msg)
            Level.FATAL -> f(tag, msg, tr)
        }
    }
    fun stop(){
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.stop()
    }
    private fun transLevel(level: Level):ch.qos.logback.classic.Level{
        return when(level){
            Level.VERBOSE -> ch.qos.logback.classic.Level.TRACE
            Level.DEBUG -> ch.qos.logback.classic.Level.DEBUG
            Level.INFO -> ch.qos.logback.classic.Level.INFO
            Level.WARN -> ch.qos.logback.classic.Level.WARN
            Level.ERROR, Level.FATAL -> ch.qos.logback.classic.Level.ERROR
        }
    }
}