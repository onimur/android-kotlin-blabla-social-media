/*
 *
 *  * Created by Murillo Comino on 01/11/19 21:23
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 01/11/19 21:23
 *
 */

package com.onimus.blablasocialmedia.mvvm.utils

import com.google.firebase.auth.FirebaseAuthException
import com.onimus.blablasocialmedia.R
import com.onimus.blablasocialmedia.mvvm.extensions.patternMatch
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth

class HandleErrors {

    fun checkEmail(email: String?): Int {
        return when {
            email.isNullOrBlank() -> {
                R.string.field_empty
            }
            !email.patternMatch(AppConstants.Pattern.EMAIL) -> {
                R.string.error_invalid_email_format
                //   authListener?.inEmailValidationError(R.string.error_invalid_email_format)

            }
            else -> -1
        }
    }

    fun checkPassword(password: String?): Int {
        return when {
            password.isNullOrBlank() -> {
                R.string.field_empty
            }
            !password.patternMatch(AppConstants.Pattern.PASSWORD) -> {
                R.string.error_password_weak
            }
            else -> -1
        }
    }

    fun getMessageError(it: Throwable): Int {
        if (it is FirebaseAuthException) {
            return when (it.errorCode) {
                ErrorFirebaseAuth.ERROR_EMAIL_ALREADY_IN_USE -> R.string.error_email_already_use
                ErrorFirebaseAuth.ERROR_INVALID_EMAIL -> R.string.error_invalid_email_format
                ErrorFirebaseAuth.ERROR_WEAK_PASSWORD -> R.string.error_password_weak
                ErrorFirebaseAuth.ERROR_CREDENTIAL_ALREADY_IN_USE -> R.string.error_credentials_already_use
                ErrorFirebaseAuth.ERROR_INVALID_CUSTOM_TOKEN -> R.string.error_invalid_custom_token
                ErrorFirebaseAuth.ERROR_CUSTOM_TOKEN_MISMATCH -> R.string.error_custom_token_mismatch
                ErrorFirebaseAuth.ERROR_INVALID_CREDENTIAL -> R.string.error_invalid_credential
                ErrorFirebaseAuth.ERROR_WRONG_PASSWORD -> R.string.error_wrong_password
                ErrorFirebaseAuth.ERROR_USER_MISMATCH -> R.string.error_user_mismatch
                ErrorFirebaseAuth.ERROR_REQUIRES_RECENT_LOGIN -> R.string.error_requires_recent_login
                ErrorFirebaseAuth.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL -> R.string.error_account_exists_with_different_credential
                ErrorFirebaseAuth.ERROR_USER_DISABLED -> R.string.error_user_disabled
                ErrorFirebaseAuth.ERROR_USER_TOKEN_EXPIRED -> R.string.error_user_token_expired
                ErrorFirebaseAuth.ERROR_USER_NOT_FOUND -> R.string.error_user_not_found
                ErrorFirebaseAuth.ERROR_INVALID_USER_TOKEN -> R.string.error_invalid_user_token
                ErrorFirebaseAuth.ERROR_OPERATION_NOT_ALLOWED -> R.string.error_operation_not_allowed
                else -> R.string.error_unknown
            }
        } else {
            return R.string.error_unknown
        }
    }
}