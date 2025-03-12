package com.schwarzit.lovenpark.profile.data

import android.content.Context
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.profile.data.local.UserDAO
import com.schwarzit.lovenpark.registration.model.Email
import com.schwarzit.lovenpark.registration.model.User
import com.schwarzit.lovenpark.registration.model.toUserRealm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserRepository {

    private val userDAO: UserDAO = UserDAO()

    /**
     * Method register and return new user if it is not already existing.
     */
    suspend fun registerUser(context: Context, user: User): User? =
        withContext(Dispatchers.IO) {
            val userRealm = user.toUserRealm()
            if (getUser(user.email) == null) {
                userDAO.saveUser(userRealm)
                UserSharedPrefHelper.saveLoggedUserEmail(context, user.email.email)
                return@withContext user
            }
            null
        }

    /**
     * Method checks if there is account associated with entered login data and returns the user.
     */

    suspend fun login(context: Context, email: String, password: String): User? =
        withContext(Dispatchers.IO) {
            val user = userDAO.getUser(Email(email))
            if (user?.email?.email == email
                && user.password.password == password
            ) {
                UserSharedPrefHelper.saveLoggedUserEmail(context, email)
                return@withContext user
            }
            null
        }

    /**
     * Method checks if there is already account associated with entered email.
     */
    suspend fun getUser(email: Email): User? = userDAO.getUser(email)

}