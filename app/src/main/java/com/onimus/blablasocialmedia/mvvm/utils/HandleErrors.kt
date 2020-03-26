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
import com.onimus.blablasocialmedia.mvvm.exception.*
import com.onimus.blablasocialmedia.mvvm.extensions.patternMatch
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorCodeFirebaseAuth
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorEmailAndPassword

class HandleErrors {

    fun filterEmail(email: String?): String {
        email.let {
            return when {
                it.isNullOrBlank() -> throw NullOrBlankEmailException()
                !it.patternMatch(AppConstants.Pattern.EMAIL) -> throw InvalidEmailException()
                else -> it
            }
        }
    }

    fun filterPassword(password: String?): String {
        password.let {
            return when {
                it.isNullOrBlank() -> throw NullOrBlankPasswordException()
                !it.patternMatch(AppConstants.Pattern.PASSWORD) -> throw InvalidPasswordException()
                else -> it
            }
        }
    }

    fun getMessageError(it: Throwable): Int {
        when (it) {
            is FirebaseAuthException -> {
                return when (it.errorCode) {
                    ErrorCodeFirebaseAuth.ERROR_EMAIL_ALREADY_IN_USE -> R.string.error_email_already_use
                    ErrorCodeFirebaseAuth.ERROR_INVALID_EMAIL -> R.string.error_invalid_email_format
                    ErrorCodeFirebaseAuth.ERROR_WEAK_PASSWORD -> R.string.error_password_weak
                    ErrorCodeFirebaseAuth.ERROR_CREDENTIAL_ALREADY_IN_USE -> R.string.error_credentials_already_use
                    ErrorCodeFirebaseAuth.ERROR_INVALID_CUSTOM_TOKEN -> R.string.error_invalid_custom_token
                    ErrorCodeFirebaseAuth.ERROR_CUSTOM_TOKEN_MISMATCH -> R.string.error_custom_token_mismatch
                    ErrorCodeFirebaseAuth.ERROR_INVALID_CREDENTIAL -> R.string.error_invalid_credential
                    ErrorCodeFirebaseAuth.ERROR_WRONG_PASSWORD -> R.string.error_wrong_password
                    ErrorCodeFirebaseAuth.ERROR_USER_MISMATCH -> R.string.error_user_mismatch
                    ErrorCodeFirebaseAuth.ERROR_REQUIRES_RECENT_LOGIN -> R.string.error_requires_recent_login
                    ErrorCodeFirebaseAuth.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL -> R.string.error_account_exists_with_different_credential
                    ErrorCodeFirebaseAuth.ERROR_USER_DISABLED -> R.string.error_user_disabled
                    ErrorCodeFirebaseAuth.ERROR_USER_TOKEN_EXPIRED -> R.string.error_user_token_expired
                    ErrorCodeFirebaseAuth.ERROR_USER_NOT_FOUND -> R.string.error_user_not_found
                    ErrorCodeFirebaseAuth.ERROR_INVALID_USER_TOKEN -> R.string.error_invalid_user_token
                    ErrorCodeFirebaseAuth.ERROR_OPERATION_NOT_ALLOWED -> R.string.error_operation_not_allowed
                    else -> R.string.error_unknown
                }
            }
            is InvalidEmailAndPasswordException -> {
                return when (it.errorCode) {
                    ErrorEmailAndPassword.NULL_OR_BLANK_EMAIL -> R.string.field_empty
                    ErrorEmailAndPassword.INVALID_EMAIL -> R.string.error_invalid_email_format
                    ErrorEmailAndPassword.NULL_OR_BLANK_PASSWORD -> R.string.field_empty
                    ErrorEmailAndPassword.INVALID_PASSWORD -> R.string.error_password_weak
                    else -> R.string.error_unknown
                }
            }
            else -> {
                return R.string.error_unknown
            }
        }
    }
}