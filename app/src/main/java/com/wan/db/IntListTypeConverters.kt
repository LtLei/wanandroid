package com.wan.db

import androidx.room.TypeConverter
import timber.log.Timber
import java.lang.NumberFormatException

object IntListTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let { d ->
            d.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException) {
                    Timber.e(ex, "Cannot convert $it to number")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }
}