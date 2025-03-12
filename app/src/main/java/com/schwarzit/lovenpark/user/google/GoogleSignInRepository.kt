package com.schwarzit.lovenpark.user.google

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.schwarzit.lovenpark.keyStore.KeyStoreManager
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys.Companion.ID_TOKEN_TOKEN_PAIR_FIRST_KEY
import com.schwarzit.lovenpark.utils.Util.Companion.SERVER_CLIENT_ID

class GoogleSignInRepository(val context: Context) {

    val keyStoreManager = KeyStoreManager(context)

    private val gso = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(SERVER_CLIENT_ID)
        .requestEmail()
        .requestId()
        .build()

    private val gsc: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    fun getSignInIntent(): Intent {
        return gsc.signInIntent
    }

    fun getAccount(context: Context){
        val account = GoogleSignIn.getLastSignedInAccount(context) ?: return
        account.idToken?.let {
            keyStoreManager.encryptToken(it,ID_TOKEN_TOKEN_PAIR_FIRST_KEY,
                KeyStoreSharedPrefsKeys.ID_TOKEN_TOKEN_PAIR_SECOND_KEY
            ) }

    }

    fun signOut(): Task<Void> {
        return gsc.signOut()
    }

}