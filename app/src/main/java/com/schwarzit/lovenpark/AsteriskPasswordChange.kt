package com.schwarzit.lovenpark

import android.text.method.PasswordTransformationMethod
import android.view.View
import com.schwarzit.lovenpark.utils.Util.Companion.ASTERISK_PASSWORD

class AsteriskPasswordChange: PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    inner class PasswordCharSequence (private val source: CharSequence) : CharSequence {

        override val length: Int
            get() = source.length

        override fun get(index: Int): Char = ASTERISK_PASSWORD

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return source.subSequence(startIndex, endIndex)
        }
    }
}