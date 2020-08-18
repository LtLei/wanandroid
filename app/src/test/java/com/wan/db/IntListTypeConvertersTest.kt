package com.wan.db

import org.junit.Assert.assertEquals
import org.junit.Test

class IntListTypeConvertersTest {
    private val intListToString = arrayListOf(1, 2, 3, 4, 5)
    private val stringToIntList = "1,2,3,4,5"

    @Test
    fun stringToIntList() {
        val converted = IntListTypeConverters.stringToIntList(stringToIntList)
        assertEquals(intListToString, converted)
    }

    @Test
    fun intListToString() {
        val converted = IntListTypeConverters.intListToString(intListToString)
        assertEquals(stringToIntList, converted)
    }
}