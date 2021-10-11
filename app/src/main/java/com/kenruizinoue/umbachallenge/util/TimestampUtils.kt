package com.kenruizinoue.umbachallenge.util

object TimestampUtils {
    fun getMinutesDifference(time1: Long, time2: Long): Int =
        convertTimestampToMinutes(time2) - convertTimestampToMinutes(time1)

    private fun convertTimestampToMinutes(timestamp: Long): Int {
        val seconds = timestamp / 1000
        val minutes = seconds / 60
        return minutes.toInt()
    }
}