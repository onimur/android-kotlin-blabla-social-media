/*
 *
 *  * Created by Murillo Comino on 25/03/20 17:50
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 25/03/20 17:50
 *
 */

package com.onimus.blablasocialmedia.mvvm.exception

/**
 * Classes for handling exceptions with email and password
 */
open class InvalidEmailAndPasswordException(message: String, private val errorCode: Int) :
    RuntimeException(message) {
    protected companion object {
        const val NULL_OR_BLANK_EMAIL = 3550
        const val INVALID_EMAIL = 3551
        const val NULL_OR_BLANK_PASSWORD = 3552
        const val INVALID_PASSWORD = 3553
    }

    fun getErrorCode() = errorCode
}

open class EmailException(message: String, errorCode: Int) :
    InvalidEmailAndPasswordException(message, errorCode)

class NullOrBlankEmailException : EmailException(MESSAGE, NULL_OR_BLANK_EMAIL) {
    companion object {
        private const val MESSAGE = "null or blank email"
    }
}

class InvalidEmailException : EmailException(MESSAGE, INVALID_EMAIL) {
    companion object {
        private const val MESSAGE = "invalid email"
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////

open class PasswordException(message: String, errorCode: Int) :
    InvalidEmailAndPasswordException(message, errorCode)

class NullOrBlankPasswordException : PasswordException(MESSAGE, NULL_OR_BLANK_PASSWORD) {
    companion object {
        private const val MESSAGE = "null or blank password"
    }
}

class InvalidPasswordException : PasswordException(MESSAGE, INVALID_PASSWORD) {
    companion object {
        private const val MESSAGE = "invalid password"
    }
}

