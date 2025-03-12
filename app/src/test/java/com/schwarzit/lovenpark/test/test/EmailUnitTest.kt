package com.schwarzit.lovenpark.test.test

import com.schwarzit.lovenpark.registration.model.Email
import com.schwarzit.lovenpark.test.utils.*
import org.junit.Assert.assertEquals
import org.junit.Test

internal class EmailUnitTest {

    @Test
    fun testEmailWithDigit() {
        assertEquals(false, Email(START_WITH_DIGIT).isDataValid())
    }

    @Test
    fun testEmailRecipientNameWithDot() {
        assertEquals(true, Email(RECIPIENT_NAMES_WITH_DOT_SEPARATE).isDataValid())
    }

    @Test
    fun testEmailOnlyOneRecipientName() {
        assertEquals(true, Email(ONLY_ONE_RECIPIENT_NAME).isDataValid())
    }

    @Test
    fun testEmailRecipientNameBiggerThanAllowed() {
        assertEquals(false, Email(RECIPIENT_NAME_BIGGER_THAN_MAX_LENGTH).isDataValid())
    }

    @Test
    fun testEmailRecipientNameMissing() {
        assertEquals(false, Email(RECIPIENT_NAME_MISSING).isDataValid())
    }

    @Test
    fun testEmailRecipientNameWithDash() {
        assertEquals(true, Email(RECIPIENT_NAMES_WITH_DASH_SEPARATE).isDataValid())
    }

    @Test
    fun testEmailRecipientNameWithApostrophe() {
        assertEquals(true, Email(RECIPIENT_NAMES_WITH_APOSTROPHE).isDataValid())
    }

    @Test
    fun testEmailRecipientNameWithDigit() {
        assertEquals(true, Email(RECIPIENT_NAMES_WITH_DIGIT).isDataValid())
    }

    @Test
    fun testEmailWithoutAtTheRateSign() {
        assertEquals(false, Email(WITHOUT_AT_THE_RATE_SIGN).isDataValid())
    }

    @Test
    fun testEmailDomainNameBiggerThanAllowed() {
        assertEquals(false, Email(DOMAIN_NAME_BIGGER_THAN_MAX_LENGTH).isDataValid())
    }

    @Test
    fun testEmailDomainNameMissing() {
        assertEquals(false, Email(DOMAIN_NAME_MISSING).isDataValid())
    }

    @Test
    fun testEmailDomainNameMissingOnlyDot() {
        assertEquals(false, Email(DOMAIN_NAME_MISSING_ONLY_DOT).isDataValid())
    }

    @Test
    fun testEmailDomainNameOneLetter() {
        assertEquals(true, Email(DOMAIN_NAME_ONE_LETTER).isDataValid())
    }

    @Test
    fun testEmailDomainNameWithDash() {
        assertEquals(true, Email(DOMAIN_NAME_WITH_DASH).isDataValid())
    }

    @Test
    fun testEmailDomainNameWithDigit() {
        assertEquals(true, Email(DOMAIN_NAME_WITH_DIGIT).isDataValid())
    }

    @Test
    fun testEmailDomainNameWithDashDigit() {
        assertEquals(true, Email(DOMAIN_NAME_WITH_DASH_DIGIT).isDataValid())
    }

    @Test
    fun testEmailTopLevelDomainOneLetter() {
        assertEquals(false, Email(TOP_LEVEL_DOMAIN_ONE_LETTER).isDataValid())
    }

    @Test
    fun testEmailTopLevelDomainTwoLetters() {
        assertEquals(true, Email(TOP_LEVEL_DOMAIN_TWO_LETTERS).isDataValid())
    }

    @Test
    fun testEmailTopLevelDomainBiggerEnough() {
        assertEquals(true, Email(TOP_LEVEL_DOMAIN_LONG_ENOUGH).isDataValid())
    }
}