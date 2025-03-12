package com.schwarzit.lovenpark.test.utils

/**
 *  @ = AT THE RATE SIGN
 *  RECIPIENT NAME = PERSONAL NAME, BEFORE @
 *  DOMAIN NAME = AFTER @ BEFORE TOP-LEVEL DOMAIN
 *  TOP-LEVEL DOMAIN = AFTER .
 */

const val START_WITH_DIGIT = "9victoria@gmai.com"
const val RECIPIENT_NAMES_WITH_DOT_SEPARATE = "Vi.ki@mail.com"
const val ONLY_ONE_RECIPIENT_NAME = "victoria@mail.com"
const val RECIPIENT_NAME_BIGGER_THAN_MAX_LENGTH =
    "Viktoria.Radoslavova.Mitrova.Internship.schwarz.it.bulgaria.sofia@mail.schwarz"
const val RECIPIENT_NAME_MISSING = "@mail.schwarz"
const val RECIPIENT_NAMES_WITH_DASH_SEPARATE = "vi-ki@mail.com"
const val RECIPIENT_NAMES_WITH_APOSTROPHE = "vi.ki'mitrova@mail.com"
const val RECIPIENT_NAMES_WITH_DIGIT = "vict0ria.mitrova@mail.com"
const val WITHOUT_AT_THE_RATE_SIGN = "victoriaMail.com"
const val DOMAIN_NAME_BIGGER_THAN_MAX_LENGTH = "viktoria@" +
        "mailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmail" +
        "mailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmailmail.com"
const val DOMAIN_NAME_MISSING = "viktoria@com"
const val DOMAIN_NAME_MISSING_ONLY_DOT = "viktoria@.com"
const val DOMAIN_NAME_ONE_LETTER = "victoria@m.com"
const val DOMAIN_NAME_WITH_DASH = "victoria@m-m.com"
const val DOMAIN_NAME_WITH_DIGIT = "victoria@2mail.com"
const val DOMAIN_NAME_WITH_DASH_DIGIT = "victoria@2mail-schwarz.com"
const val TOP_LEVEL_DOMAIN_ONE_LETTER = "victoria@mail.s"
const val TOP_LEVEL_DOMAIN_TWO_LETTERS = "victoria@mail.sh"
const val TOP_LEVEL_DOMAIN_LONG_ENOUGH = "victoria@mail.schwarz"

