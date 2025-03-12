package com.schwarzit.lovenpark.test.test

import com.schwarzit.lovenpark.registration.model.PhoneNumber
import com.schwarzit.lovenpark.test.utils.*
import org.junit.Test
import org.junit.Assert.*

internal class PhoneNumberUnitTest {
    @Test
    fun testPhoneNumberStartWithPlus() {
        assertEquals(true, PhoneNumber(START_WITH_PLUS).isDataValid())
    }

    @Test
    fun testPhoneNumberStartWithPlusDash() {
        assertEquals(true, PhoneNumber(START_WITH_PLUS_DASH).isDataValid())
    }

    @Test
    fun testPhoneNumberStartWithPlusWithoutSpaces() {
        assertEquals(true, PhoneNumber(START_WITH_PLUS_WITHOUT_SPACES).isDataValid())
    }

    @Test
    fun testPhoneNumberStartWithPlusSpaceDash() {
        assertEquals(true, PhoneNumber(START_WITH_PLUS_SPACE_DASH).isDataValid())
    }

    @Test
    fun testPhoneNumberStartWithZeroSpace() {
        assertEquals(true, PhoneNumber(START_WITH_ZERO_SPACE).isDataValid())
    }

    @Test
    fun testPhoneNumberStartWithZeroWithoutSpaces() {
        assertEquals(true, PhoneNumber(START_WITH_ZERO_WITHOUT_SPACES).isDataValid())
    }

    @Test
    fun testPhoneNumberMaxLengthWithSpaces() {
        assertEquals(false, PhoneNumber(MAX_LENGTH_WITH_SPACES).isDataValid())
    }

    @Test
    fun testPhoneNumberMinLengthWithoutSpaces() {
        assertEquals(false, PhoneNumber(MIN_LENGTH_WITHOUT_SPACES).isDataValid())
    }

    @Test
    fun testPhoneNumberMinLengthWithSpaces() {
        assertEquals(false, PhoneNumber(MIN_LENGTH_WITH_SPACES).isDataValid())
    }

    @Test
    fun testPhoneNumberStartWithZeroAndSpaces() {
        assertEquals(true, PhoneNumber(START_WITH_ZERO_SPACES).isDataValid())
    }

    @Test
    fun testPhoneNumberStartWithZero() {
        assertEquals(false, PhoneNumber(START_WITH_ZERO).isDataValid())
    }

    @Test
    fun testPhoneNumberMaxLengthWithoutSpaces() {
        assertEquals(true, PhoneNumber(MAX_LENGTH_WITHOUT_SPACES).isDataValid())
    }

    @Test
    fun testPhoneNumberWithOtherSymbol() {
        assertEquals(false, PhoneNumber(WITH_OTHER_SYMBOLS).isDataValid())
    }

}