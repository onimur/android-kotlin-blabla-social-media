/*
 *
 *  * Created by Murillo Comino on 28/10/19 22:32
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 28/10/19 22:32
 *
 */

package com.onimus.blablasocialmedia.mvvm.commons

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.onimus.blablasocialmedia.R
import com.onimus.blablasocialmedia.mvvm.data.firebase.FirebaseManager
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AuthViewModelIT {


    companion object {
        const val CORRECT_EMAIL = "mur@gmail.com"
        const val CORRECT_PASSWORD = "Abc123"
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var authViewModel: AuthViewModel
    @MockK
    private lateinit var mockUserRepository: UserRepository
    @MockK
    private lateinit var mockFirebaseManager: FirebaseManager
    @MockK
    private var mockAuthListener: AuthListener = mockk(relaxed = true)
    @MockK
    var mockAuthTask: Task<AuthResult> = mockk(relaxed = true)
    @MockK
    var mockVoidTask: Task<Void> = mockk(relaxed = true)
    @MockK
    private lateinit var mockAuth: FirebaseAuth

    //Captor
    private val slotCompleteListener = slot<OnCompleteListener<AuthResult>>()
    private val slotFailureListener = slot<OnFailureListener>()

    private val slotCompleteListenerReset = slot<OnCompleteListener<Void>>()
    private val slotFailureListenerReset = slot<OnFailureListener>()

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        MockKAnnotations.init(this)
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)

        setupMocks()

        setupTasks()
    }

    private fun setupMocks() {
        mockAuth = FirebaseAuth.getInstance()
        mockFirebaseManager = FirebaseManager()
        mockUserRepository = UserRepository(mockFirebaseManager)
        authViewModel =
            AuthViewModel(mockUserRepository, Schedulers.trampoline(), Schedulers.trampoline())
        authViewModel.authListener = mockAuthListener
        authViewModel.email = CORRECT_EMAIL
        authViewModel.password = CORRECT_PASSWORD
    }

    private fun setupTasks() {
        every {
            mockAuth.createUserWithEmailAndPassword(
                CORRECT_EMAIL,
                CORRECT_PASSWORD
            )
        } returns mockAuthTask

        every {
            mockAuth.signInWithEmailAndPassword(
                CORRECT_EMAIL,
                CORRECT_PASSWORD
            )
        } returns mockAuthTask

        every {
            mockAuth.sendPasswordResetEmail(
                CORRECT_EMAIL
            )
        } returns mockVoidTask

        every { mockAuthTask.addOnCompleteListener(capture(slotCompleteListener)) } returns mockAuthTask
        every { mockAuthTask.addOnFailureListener(capture(slotFailureListener)) } returns mockAuthTask

        every { mockVoidTask.addOnCompleteListener(capture(slotCompleteListenerReset)) } returns mockVoidTask
        every { mockVoidTask.addOnFailureListener(capture(slotFailureListenerReset)) } returns mockVoidTask

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `the first time getProgressBarStatus is called should return false`() {
        assertFalse(authViewModel.getProgressBarStatus().value!!)
    }

    @Test
    fun `getProgressBarStatus should return true`() {
        authViewModel.setProgressBarStatus(true)
        assertTrue(authViewModel.getProgressBarStatus().value!!)
    }

    @Test
    fun `onClickTextViewLogin should call onNavigate`() {
        authViewModel.onClickTextViewLogin()
        verify(exactly = 1) { mockAuthListener.onNavigate() }
    }

    @Test
    fun `onClickTextViewRegister should call onClickTextViewRegister`() {
        authViewModel.onClickTextViewRegister()
        verify(exactly = 1) { mockAuthListener.onClickTextViewRegister() }
    }

    @Test
    fun `onClickTextViewForgotPassword should call onClickTextViewForgotPassword`() {
        authViewModel.onClickTextViewForgotPassword()
        verify(exactly = 1) { mockAuthListener.onClickTextViewForgotPassword() }
    }
    /////////////////////////////////////////////////////////////////////////////////////
    /**
     * Testing methods related to the rxjava2 library.
     * Events Tested: Click the "Register" button and click the "Log In" button
     */
    @Test
    fun `onClickButtonRegister should be isSuccessful`() {
        authViewModel.onClickButtonRegister()

        every { mockAuthTask.isSuccessful } returns true
        slotCompleteListener.captured.onComplete(mockAuthTask)
        verify {
            mockAuth.createUserWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify(exactly = 2) { mockAuthListener.resetTextInputLayout() }
        verify(exactly = 1) { mockAuthListener.showProgress() }
        verify(exactly = 1) { mockAuthListener.hideProgress() }
        verify(exactly = 1) { mockAuthListener.onSuccessAuth() }
    }

    @Test
    fun `onClickButtonRegister should be return error with FirebaseAuthException`() {
        authViewModel.onClickButtonRegister()

        slotFailureListener.captured.onFailure(
            FirebaseAuthWeakPasswordException(
                AppConstants.ErrorFirebaseAuth.ERROR_REQUIRES_RECENT_LOGIN,
                "test2",
                "test3"
            )
        )

        verify {
            mockAuth.createUserWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify(exactly = 1) { mockAuthListener.onFailureAuth(R.string.error_requires_recent_login) }

    }

    @Test
    fun `onClickButtonRegister should be return unknown error with other Exception`() {
        authViewModel.onClickButtonRegister()

        slotFailureListener.captured.onFailure(Exception())
        verify {
            mockAuth.createUserWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify { mockAuthListener.onFailureAuth(R.string.error_unknown) }
    }

    @Test
    fun `onClickButtonLogin should be isSuccessful`() {
        authViewModel.onClickButtonLogin()

        every { mockAuthTask.isSuccessful } returns true
        slotCompleteListener.captured.onComplete(mockAuthTask)
        verify {
            mockAuth.signInWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify(exactly = 2) { mockAuthListener.resetTextInputLayout() }
        verify(exactly = 1) { mockAuthListener.showProgress() }
        verify(exactly = 1) { mockAuthListener.hideProgress() }
        verify(exactly = 1) { mockAuthListener.onSuccessAuth() }
    }

    @Test
    fun `onClickButtonLogin should be return error with FirebaseAuthException`() {
        authViewModel.onClickButtonLogin()

        slotFailureListener.captured.onFailure(
            FirebaseAuthWeakPasswordException(
                AppConstants.ErrorFirebaseAuth.ERROR_REQUIRES_RECENT_LOGIN,
                "test2",
                "test3"
            )
        )

        verify {
            mockAuth.signInWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify(exactly = 1) { mockAuthListener.onFailureAuth(R.string.error_requires_recent_login) }

    }

    @Test
    fun `onClickButtonLogin should be return unknown error with other Exception`() {
        authViewModel.onClickButtonLogin()

        slotFailureListener.captured.onFailure(Exception())
        verify {
            mockAuth.signInWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify { mockAuthListener.onFailureAuth(R.string.error_unknown) }
    }

    @Test
    fun `onClickButtonReset should be isSuccessful`() {
        authViewModel.onClickButtonReset()

        every { mockVoidTask.isSuccessful } returns true
        slotCompleteListenerReset.captured.onComplete(mockVoidTask)
        verify {
            mockAuth.sendPasswordResetEmail(
                eq(CORRECT_EMAIL)
            )
        }
        verify(exactly = 2) { mockAuthListener.resetTextInputLayout() }
        verify(exactly = 1) { mockAuthListener.showProgress() }
        verify(exactly = 1) { mockAuthListener.hideProgress() }
        verify(exactly = 1) { mockAuthListener.onSuccessAuth() }
    }

    @Test
    fun `onClickButtonReset should be return error with FirebaseAuthException`() {
        authViewModel.onClickButtonReset()

        slotFailureListenerReset.captured.onFailure(
            FirebaseAuthInvalidCredentialsException(
                AppConstants.ErrorFirebaseAuth.ERROR_USER_NOT_FOUND,
                "test2"
            )
        )

        verify {
            mockAuth.sendPasswordResetEmail(
                eq(CORRECT_EMAIL)
            )
        }
        verify(exactly = 1) { mockAuthListener.onFailureAuth(R.string.error_user_not_found) }

    }

    @Test
    fun `onClickButtonReset should be return unknown error with other Exception`() {
        authViewModel.onClickButtonReset()

        slotFailureListenerReset.captured.onFailure(Exception())
        verify {
            mockAuth.sendPasswordResetEmail(
                eq(CORRECT_EMAIL)
            )
        }
        verify { mockAuthListener.onFailureAuth(R.string.error_unknown) }
    }

    @Test
    fun `onClickButtonRegister with invalid email should be call inEmailValidationError`() {
        authViewModel.email = "adddss.com"

        authViewModel.onClickButtonRegister()
        verify(exactly = 1) { mockAuthListener.inEmailValidationError(R.string.error_invalid_email_format) }
    }

    @Test
    fun `onClickButtonRegister with invalid password should be call inPasswordValidationError`() {
        authViewModel.password = "123456"

        authViewModel.onClickButtonRegister()
        verify(exactly = 1) { mockAuthListener.inPasswordValidationError(R.string.error_password_weak) }
    }

    @Test
    fun `onClickButtonLogin with invalid email should be call inEmailValidationError`() {
        authViewModel.email = "adddss.com"

        authViewModel.onClickButtonLogin()
        verify(exactly = 1) { mockAuthListener.inEmailValidationError(R.string.error_invalid_email_format) }
    }

    @Test
    fun `onClickButtonLogin with invalid password should be call inPasswordValidationError`() {
        authViewModel.password = "123456"

        authViewModel.onClickButtonLogin()
        verify(exactly = 1) { mockAuthListener.inPasswordValidationError(R.string.error_password_weak) }
    }

    @Test
    fun `onClickButtonReset with invalid email should be call inEmailValidationError`() {
        authViewModel.email = "adddss.com"

        authViewModel.onClickButtonReset()
        verify(exactly = 1) { mockAuthListener.inEmailValidationError(R.string.error_invalid_email_format) }
    }
}