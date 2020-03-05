package com.photo.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by sashiro on 2018/4/17.
 */

object TimeUtil {

    const val EMPTY_DATE: Long = -1L

    fun formatToSelectStyle(dateTime: Long): String =
        formatTime(dateTime, "yyyy年MM月")

    fun formatToSettingsStyle(dateTime: Long): String =
        if (dateTime < 0) "" else formatTime(dateTime, "yyyy.MM.dd")

    fun convertLocalTime(
        dateStr: String,
        default: Long = Calendar.getInstance().timeInMillis
    ): Long =
        dateStr.toLongOrNull() ?: default

    fun convertExifTime(
        dateStr: String,
        default: Long = Calendar.getInstance().timeInMillis
    ): Long =
        doParseOrDefault(default) {
            SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.JAPAN).parse(dateStr).time
        }

    //check the Pattern
    fun convertFacebookTime(dateStr: String): Long =
        when (dateStr.isNotBlank()) {
            true -> doParseOrDefault {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.JAPAN).parse(dateStr).time
            }
            false -> Calendar.getInstance().timeInMillis
        }

    fun convertGoogleTime(
        dateStr: String,
        default: Long = Calendar.getInstance().timeInMillis
    ): Long =
        if (dateStr.isNotBlank()) doParseOrDefault(default) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.JAPAN).parse(dateStr).time
        }
        else default

    fun formatTime(dateTime: Long, format: String): String =
        SimpleDateFormat(format, Locale.JAPAN).format(Date().apply { time = dateTime })

    private fun doParseOrDefault(
        default: Long = Calendar.getInstance().timeInMillis,
        target: () -> Long
    ): Long {
        return try {
            target()
        } catch (e: ParseException) {
            default
        }
    }
}