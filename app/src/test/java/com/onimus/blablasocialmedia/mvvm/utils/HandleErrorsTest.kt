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

import com.google.firebase.auth.*
import com.onimus.blablasocialmedia.R
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class HandleErrorsTest {

    companion object {
        const val CORRECT_EMAIL = "mur@gmail.com"
        const val CORRECT_PASSWORD = "Abc123"
    }

    private lateinit var handleErrors: HandleErrors

    @Before
    fun setUp() {
        handleErrors = HandleErrors()
    }

    @Test
    fun `checkEmail with empty or blank should return error`() {
        assertThat(handleErrors.checkEmail(null), `is`(R.string.field_empty))
        assertThat(handleErrors.checkEmail("  "), `is`(R.string.field_empty))
    }

    @Test
    fun `checkPassword with empty or blank should return error`() {
        assertThat(handleErrors.checkPassword(null), `is`(R.string.field_empty))
        assertThat(handleErrors.checkPassword("  "), `is`(R.string.field_empty))
    }

    @Test
    fun `checkEmail with invalid format should return error`() {
        assertThat(
            handleErrors.checkEmail("2s2s2sd2r2.com"),
            `is`(R.string.error_invalid_email_format)
        )
        assertThat(
            handleErrors.checkEmail("s2s @!dsq%@.gmail.com"),
            `is`(R.string.error_invalid_email_format)
        )

    }

    @Test
    fun `checkPassword with invalid format should return error`() {
        val error = R.string.error_password_weak

        //without 1 character uppercase
        assertThat(handleErrors.checkPassword("abc123"), `is`(error))
        //without 1 numeric character
        assertThat(handleErrors.checkPassword("abcdef"), `is`(error))
        //without alphabetic characters
        assertThat(handleErrors.checkPassword("123456"), `is`(error))
        //without alphanumeric characters
        assertThat(handleErrors.checkPassword("$%#@!&"), `is`(error))
        //with less than 6 characters
        assertThat(handleErrors.checkPassword("Abc12"), `is`(error))

    }

    @Test
    fun `checkEmail and checkPassword with valid format should return -1`() {
        assertThat(handleErrors.checkEmail(CORRECT_EMAIL), `is`(-1))
        assertThat(handleErrors.checkPassword(CORRECT_PASSWORD), `is`(-1))
    }
    /////////////////////////////////////////////////////////////////////////////

    @Test
    fun `getMessageError with FirebaseAuthException or to extend it should return error`() {
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthEmailException(
                    AppConstants.ErrorFirebaseAuth.ERROR_EMAIL_ALREADY_IN_USE,
                    ""
                )
            ), `is`(R.string.error_email_already_use)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthActionCodeException(
                    AppConstants.ErrorFirebaseAuth.ERROR_INVALID_EMAIL,
                    ""
                )
            ), `is`(R.string.error_invalid_email_format)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthEmailException(
                    AppConstants.ErrorFirebaseAuth.ERROR_WEAK_PASSWORD,
                    ""
                )
            ), `is`(R.string.error_password_weak)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthInvalidCredentialsException(
                    AppConstants.ErrorFirebaseAuth.ERROR_CREDENTIAL_ALREADY_IN_USE,
                    ""
                )
            ), `is`(R.string.error_credentials_already_use)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthRecentLoginRequiredException(
                    AppConstants.ErrorFirebaseAuth.ERROR_INVALID_CUSTOM_TOKEN,
                    ""
                )
            ), `is`(R.string.error_invalid_custom_token)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthWebException(
                    AppConstants.ErrorFirebaseAuth.ERROR_CUSTOM_TOKEN_MISMATCH,
                    ""
                )
            ), `is`(R.string.error_custom_token_mismatch)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_INVALID_CREDENTIAL,
                    ""
                )
            ), `is`(R.string.error_invalid_credential)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_WRONG_PASSWORD,
                    ""
                )
            ), `is`(R.string.error_wrong_password)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_USER_MISMATCH,
                    ""
                )
            ), `is`(R.string.error_user_mismatch)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_REQUIRES_RECENT_LOGIN,
                    ""
                )
            ), `is`(R.string.error_requires_recent_login)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL,
                    ""
                )
            ), `is`(R.string.error_account_exists_with_different_credential)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_USER_DISABLED,
                    ""
                )
            ), `is`(R.string.error_user_disabled)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_USER_TOKEN_EXPIRED,
                    ""
                )
            ), `is`(R.string.error_user_token_expired)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_USER_NOT_FOUND,
                    ""
                )
            ), `is`(R.string.error_user_not_found)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_INVALID_USER_TOKEN,
                    ""
                )
            ), `is`(R.string.error_invalid_user_token)
        )
        assertThat(
            handleErrors.getMessageError(
                FirebaseAuthException(
                    AppConstants.ErrorFirebaseAuth.ERROR_OPERATION_NOT_ALLOWED,
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
}