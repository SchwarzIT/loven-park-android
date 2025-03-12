package com.schwarzit.lovenpark.test.test

import com.schwarzit.lovenpark.registration.model.Password
import com.schwarzit.lovenpark.test.utils.*
import org.junit.Assert
import org.junit.Test

internal class PasswordUnitTest {

    @Test
    fun testPassword() {
        Assert.assertEquals(true, Password(VALID_PASSWORD).isDataValid())
    }

    @Test
    fun testPasswordWithSpaceForSpecialSymbol() {
        Assert.assertEquals(true, Password(WITH_SPACE_FOR_SPACIAL_SYMBOL).isDataValid())
    }

    @Test
    fun testPasswordMissingUpperLetter() {
        Assert.assertEquals(false, Password(MISSING_UPPER_LETTER).isDataValid())
    }

    @Test
    fun testPasswordMissingLowerLetter() {
        Assert.assertEquals(false, Password(MISSING_LOWER_LETTER).isDataValid())
    }

    @Test
    fun testPasswordMissingDigit() {
        Assert.assertEquals(false, Password(MISSING_DIGIT).isDataValid())
    }

    @Test
    fun testPasswordMissingSpecialSymbol() {
        Assert.assertEquals(false, Password(MISSING_SPECIAL_SYMBOL).isDataValid())
    }

    @Test
    fun testPasswordNoMinLength() {
        Assert.assertEquals(false, Password(NO_MIN_LENGTH).isDataValid())
    }
}