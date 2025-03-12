package com.schwarzit.lovenpark

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class GoogleSignInMapper {
    fun mapGoogleSignInAccountToGoogleAccount(account: GoogleSignInAccount): GoogleAccount {
        return GoogleAccount(
            account.id,
            account.idToken,
            account.givenName,
            account.familyName,
            account.displayName
        )
    }
}