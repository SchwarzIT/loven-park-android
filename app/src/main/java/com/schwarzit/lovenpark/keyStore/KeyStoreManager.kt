package com.schwarzit.lovenpark.keyStore

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys.Companion.ANDROID_KEY_STORE
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys.Companion.CIPHER_TRANSFORMATION
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys.Companion.KEY_ALIAS
import com.schwarzit.lovenpark.utils.Util.Companion.EMPTY_STRING
import com.schwarzit.lovenpark.utils.Util.Companion.ENCRYPT_STANDARD_SYMBOLS
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class KeyStoreManager(val context: Context) {

    private var pair: Pair<ByteArray, ByteArray> = encryptData(EMPTY_STRING)

    private val String.toPreservedByteArray: ByteArray
        get() {
            return this.toByteArray(Charsets.ISO_8859_1)
        }

    private val ByteArray.toPreservedString: String
        get() {
            return String(this, Charsets.ISO_8859_1)
        }

    private fun generateKey(): SecretKey{
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE
        )
        val keyGeneratorParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT
                    or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGeneratorParameterSpec)
        return keyGenerator.generateKey()
    }

    private fun getKey(): SecretKey {
        val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)

        val secretKeyEntry = keyStore.getEntry(KEY_ALIAS, null)
                as? KeyStore.SecretKeyEntry
        return secretKeyEntry?.secretKey ?: generateKey()
    }

    private fun encryptData(data: String): Pair<ByteArray, ByteArray> {
        val cipher: Cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        var temp = data
        while (temp.toByteArray().size % 16 != 0)
            temp += ENCRYPT_STANDARD_SYMBOLS
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val ivBytes = cipher.iv

        val encryptedBytes = cipher.doFinal(temp.toByteArray(Charsets.UTF_8))
        return Pair(ivBytes, encryptedBytes)
    }

    private fun decryptData(ivBytes: ByteArray, data: ByteArray): String {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val spec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }

    fun encryptToken(
        token: String,
        tokenPairFirstKey: String,
        tokenPairSecondKey: String
    ):
            Pair<ByteArray, ByteArray> {
        pair = encryptData(token)
        val firstPair = pair.first.toPreservedString
        val secondPair = pair.second.toPreservedString
        KeyStoreSharedPrefManager.saveTokenPairString(context, firstPair, tokenPairFirstKey)
        KeyStoreSharedPrefManager.saveTokenPairString(context, secondPair, tokenPairSecondKey)
        return pair
    }

    fun decryptToken(
        tokenPairFirstKey: String,
        tokenPairSecondKey: String
    ): String {
        val firstPair = KeyStoreSharedPrefManager.getTokenPairString(context, tokenPairFirstKey)
        val secondPair = KeyStoreSharedPrefManager.getTokenPairString(context, tokenPairSecondKey)
        if (firstPair?.isNotEmpty() == true && secondPair?.isNotEmpty() == true) {
            val firstByteArray = firstPair.toPreservedByteArray
            val secondByteArray = secondPair.toPreservedByteArray
            return decryptData(firstByteArray, secondByteArray)
        }
        return decryptData(pair.first, pair.second)
    }

    fun clearToken(tokenPairFirstKey: String, tokenPairSecondKey: String) {
        pair = encryptData(EMPTY_STRING)
        KeyStoreSharedPrefManager.saveTokenPairString(context, EMPTY_STRING, tokenPairFirstKey)
        KeyStoreSharedPrefManager.saveTokenPairString(context, EMPTY_STRING, tokenPairSecondKey)
    }

}