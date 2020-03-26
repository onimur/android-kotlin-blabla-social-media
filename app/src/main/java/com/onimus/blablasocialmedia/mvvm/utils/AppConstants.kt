/*
 *
 *  * Created by Murillo Comino on 26/10/19 19:36
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 19:36
 *
 */

package com.onimus.blablasocialmedia.mvvm.utils

object AppConstants {

    object Tag {
        const val LOG_W = "Onimus/warning"
        const val LOG_D = "Onimus/check"
    }

    const val RC_SIGN_IN = 100

    object ErrorCodeFirebaseAuth {
        const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
        const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
        const val ERROR_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD"
        const val ERROR_CREDENTIAL_ALREADY_IN_USE = "ERROR_CREDENTIAL_ALREADY_IN_USE"
        const val ERROR_INVALID_CUSTOM_TOKEN = "ERROR_INVALID_CUSTOM_TOKEN"
        const val ERROR_CUSTOM_TOKEN_MISMATCH = "ERROR_CUSTOM_TOKEN_MISMATCH"
        const val ERROR_INVALID_CREDENTIAL = "ERROR_INVALID_CREDENTIAL"
        const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
        const val ERROR_USER_MISMATCH = "ERROR_USER_MISMATCH"
        const val ERROR_REQUIRES_RECENT_LOGIN = "ERROR_REQUIRES_RECENT_LOGIN"
        const val ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL =
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL"
        const val ERROR_USER_DISABLED = "ERROR_USER_DISABLED"
        const val ERROR_USER_TOKEN_EXPIRED = "ERROR_USER_TOKEN_EXPIRED"
        const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
        const val ERROR_INVALID_USER_TOKEN = "ERROR_INVALID_USER_TOKEN"
        const val ERROR_OPERATION_NOT_ALLOWED = "ERROR_OPERATION_NOT_ALLOWED"
    }

    object ErrorEmailAndPassword {
        const val NULL_OR_BLANK_EMAIL = 3550
        const val INVALID_EMAIL = 3551
        const val NULL_OR_BLANK_PASSWORD = 3552
        const val INVALID_PASSWORD = 3553
    }

    object Pattern {
        val EMAIL: java.util.regex.Pattern = java.util.regex.Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        val PASSWORD: java.util.regex.Pattern = java.util.regex.Pattern.compile(
            "^" +                     //start-of-string
                    "(?=.*[0-9])" +         //a digit must occur at least once
                    "(?=.*[a-z])" +         //a lower case letter must occur at least once
                    "(?=.*[A-Z])" +         //an upper case letter must occur at least once
                    //"(?=.*[@#$%^&+=])" +  //a special character must occur at least once replace with your special characters
                    "(?=\\S+$)" +           //no whitespace allowed in the entire string
                    ".{6,}" +               //anything, at least six places though
                    "$"                     //end-of-string
        )
    }
}