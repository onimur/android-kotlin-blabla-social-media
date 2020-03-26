/*
 *
 *  * Created by Murillo Comino on 01/11/19 21:55
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 01/11/19 21:55
 *
 */

package com.onimus.blablasocialmedia.mvvm.utils


import com.onimus.blablasocialmedia.mvvm.exception.*
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.*
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL_BLANK
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL_EMPTY
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL_INVALID
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL_INVALID2
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL_INVALID3
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD_BLANK
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD_EMPTY
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD_INVALID
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD_INVALID2
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD_INVALID3
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD_INVALID4
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD_INVALID5
import org.assertj.core.api.Assertions.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsEqual

import org.junit.Before
import org.junit.Test
import java.lang.AssertionError
import java.util.function.Predicate
import java.util.function.Predicate.isEqual


class HandleErrorsTest {

    companion object {
        const val NULL_OR_BLANK_EMAIL = 3550
        const val INVALID_EMAIL = 3551
        const val NULL_OR_BLANK_PASSWORD = 3552
        const val INVALID_PASSWORD = 3553

        private const val MESSAGE_NULL_OR_BLANK_EMAIL = "null or blank email"
        private const val MESSAGE_NULL_OR_BLANK_PASSWORD = "null or blank password"
        private const val MESSAGE_INVALID_EMAIL = "invalid email"
        private const val MESSAGE_INVALID_PASSWORD = "invalid password"
    }

    private lateinit var handleErrors: HandleErrors

    @Before
    fun setUp() {
        handleErrors = HandleErrors()
    }

    @Test
    fun `filterEmail with null, empty or blank email should return NullOrBlankEmailException`() {

        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterEmail(null) }
            .withMessageContaining(MESSAGE_NULL_OR_BLANK_EMAIL)
            .isNotInstanceOf(PasswordException::class.java)
            .isInstanceOf(NullOrBlankEmailException::class.java)

        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterEmail(EMAIL_EMPTY) }
            .withMessageContaining(MESSAGE_NULL_OR_BLANK_EMAIL)
            .isNotInstanceOf(PasswordException::class.java)
            .isInstanceOf(NullOrBlankEmailException::class.java)

        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterEmail(EMAIL_BLANK) }
            .withMessageContaining(MESSAGE_NULL_OR_BLANK_EMAIL)
            .isNotInstanceOf(PasswordException::class.java)
            .isInstanceOf(NullOrBlankEmailException::class.java)

    }

    @Test
    fun `filterPassword with null, empty or blank password should return NullOrBlankPasswordException`() {
        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterPassword(null) }
            .withMessageContaining(MESSAGE_NULL_OR_BLANK_PASSWORD)
            .isNotInstanceOf(EmailException::class.java)
            .isInstanceOf(NullOrBlankPasswordException::class.java)

        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterPassword(PASSWORD_EMPTY) }
            .withMessageContaining(MESSAGE_NULL_OR_BLANK_PASSWORD)
            .isNotInstanceOf(EmailException::class.java)
            .isInstanceOf(NullOrBlankPasswordException::class.java)

        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterPassword(PASSWORD_BLANK) }
            .withMessageContaining(MESSAGE_NULL_OR_BLANK_PASSWORD)
            .isNotInstanceOf(EmailException::class.java)
            .isInstanceOf(NullOrBlankPasswordException::class.java)
    }

    @Test
    fun `filterEmail with invalid format should return InvalidEmailException`() {
        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterEmail(EMAIL_INVALID) }
            .withMessageContaining(MESSAGE_INVALID_EMAIL)
            .isNotInstanceOf(PasswordException::class.java)
            .isInstanceOf(InvalidEmailException::class.java)

        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterEmail(EMAIL_INVALID2) }
            .withMessageContaining(MESSAGE_INVALID_EMAIL)
            .isNotInstanceOf(PasswordException::class.java)
            .isInstanceOf(InvalidEmailException::class.java)

        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterEmail(EMAIL_INVALID3) }
            .withMessageContaining(MESSAGE_INVALID_EMAIL)
            .isNotInstanceOf(PasswordException::class.java)
            .isInstanceOf(InvalidEmailException::class.java)
    }

    @Test
    fun `filterPassword with invalid format should return InvalidPasswordException`() {
        //without 1 character uppercase
        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterPassword(PASSWORD_INVALID) }
            .withMessageContaining(MESSAGE_INVALID_PASSWORD)
            .isNotInstanceOf(EmailException::class.java)
            .isInstanceOf(InvalidPasswordException::class.java)
        //without 1 numeric character
        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterPassword(PASSWORD_INVALID2) }
            .withMessageContaining(MESSAGE_INVALID_PASSWORD)
            .isNotInstanceOf(EmailException::class.java)
            .isInstanceOf(InvalidPasswordException::class.java)
        //without alphabetic characters
        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterPassword(PASSWORD_INVALID3) }
            .withMessageContaining(MESSAGE_INVALID_PASSWORD)
            .isNotInstanceOf(EmailException::class.java)
            .isInstanceOf(InvalidPasswordException::class.java)
        //without alphanumeric characters
        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterPassword(PASSWORD_INVALID4) }
            .withMessageContaining(MESSAGE_INVALID_PASSWORD)
            .isNotInstanceOf(EmailException::class.java)
            .isInstanceOf(InvalidPasswordException::class.java)
        //with less than 6 characters
        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy { handleErrors.filterPassword(PASSWORD_INVALID5) }
            .withMessageContaining(MESSAGE_INVALID_PASSWORD)
            .isNotInstanceOf(EmailException::class.java)
            .isInstanceOf(InvalidPasswordException::class.java)
    }
/*
    @Test
    fun `checkEmail and checkPassword with valid format should return -1`() {
        assertThat(handleErrors.filterEmail(CORRECT_EMAIL), `is`(-1))
        assertThat(handleErrors.filterPassword(CORRECT_PASSWORD), `is`(-1))
    }
    /////////////////////////////////////////////////////////////////////////////

    @Test
    fun `getMessageError with FirebaseAuthException or to extend it should return error`() {
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthEmailException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_EMAIL_ALREADY_IN_USE,
                    ""
                )
            ), `is`(R.string.error_email_already_use)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthActionCodeException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_INVALID_EMAIL,
                    ""
                )
            ), `is`(R.string.error_invalid_email_format)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthEmailException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_WEAK_PASSWORD,
                    ""
                )
            ), `is`(R.string.error_password_weak)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthInvalidCredentialsException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_CREDENTIAL_ALREADY_IN_USE,
                    ""
                )
            ), `is`(R.string.error_credentials_already_use)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthRecentLoginRequiredException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_INVALID_CUSTOM_TOKEN,
                    ""
                )
            ), `is`(R.string.error_invalid_custom_token)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthWebException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_CUSTOM_TOKEN_MISMATCH,
                    ""
                )
            ), `is`(R.string.error_custom_token_mismatch)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_INVALID_CREDENTIAL,
                    ""
                )
            ), `is`(R.string.error_invalid_credential)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_WRONG_PASSWORD,
                    ""
                )
            ), `is`(R.string.error_wrong_password)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_USER_MISMATCH,
                    ""
                )
            ), `is`(R.string.error_user_mismatch)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_REQUIRES_RECENT_LOGIN,
                    ""
                )
            ), `is`(R.string.error_requires_recent_login)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL,
                    ""
                )
            ), `is`(R.string.error_account_exists_with_different_credential)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_USER_DISABLED,
                    ""
                )
            ), `is`(R.string.error_user_disabled)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_USER_TOKEN_EXPIRED,
                    ""
                )
            ), `is`(R.string.error_user_token_expired)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_USER_NOT_FOUND,
                    ""
                )
            ), `is`(R.string.error_user_not_found)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_INVALID_USER_TOKEN,
                    ""
                )
            ), `is`(R.string.error_invalid_user_token)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorCodeFirebaseAuth.ERROR_OPERATION_NOT_ALLOWED,
                    ""
                )
            ), `is`(R.string.error_operation_not_allowed)
        )
        assertThat(
            handleErrors.getMessageError(FirebaseAuthException(anyString(), "")),
            `is`(R.string.error_unknown)
        )
    }

    @Test
    fun `getMessageError with other Exception should return unknown error`() {
        assertThat(
            handleErrors.getMessageError(ArrayStoreException("")),
            `is`(R.string.error_unknown)
        )
        assertThat(
            handleErrors.getMessageError(KotlinNullPointerException()),
            `is`(R.string.error_unknown)
        )
    }

 */
}

