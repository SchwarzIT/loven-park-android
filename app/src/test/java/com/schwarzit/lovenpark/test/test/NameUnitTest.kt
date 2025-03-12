package com.schwarzit.lovenpark.test.test

import com.schwarzit.lovenpark.registration.model.Name
import com.schwarzit.lovenpark.test.utils.*
import org.junit.Assert.assertEquals
import org.junit.Test

internal class NameUnitTest {
    @Test
    fun testNameOnlyLetters() {
        assertEquals(true, Name(ONLY_LETTERS).isDataValid())
    }

    @Test
    fun testNameWithApostrophe() {
        assertEquals(true, Name(WITH_APOSTROPHE).isDataValid())
    }

    @Test
    fun testNameWithDigits() {
        assertEquals(false, Name(WITH_DIGIT).isDataValid())
    }

    @Test
    fun testNameWithDash() {
        assertEquals(true, Name(WITH_DASH).isDataValid())
    }

    @Test
    fun testNameWithDashAndSpace() {
        assertEquals(true, Name(WITH_DASH_SPACE).isDataValid())
    }

    @Test
    fun testNameWithOtherSymbols() {
        assertEquals(false, Name(WITH_OTHER_SYMBOL).isDataValid())
    }

    @Test
    fun testNameStartWithDash() {
        assertEquals(false, Name(START_WITH_DASH).isDataValid())
    }

    @Test
    fun testNameStartWithApostrophe() {
        assertEquals(false, Name(START_WITH_APOSTROPHE).isDataValid())
    }

    @Test
    fun testNameStartWithApostropheAndDash() {
        assertEquals(true, Name(WITH_APOSTROPHE_DASH).isDataValid())
    }
}