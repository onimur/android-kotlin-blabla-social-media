/*
 *
 *  * Created by Murillo Comino on 02/03/20 20:27
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 20/02/20 21:20
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.login

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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
import org.junit.Before
import org.junit.Test


class LoginViewModelIT {

    companion object {
        const val CORRECT_EMAIL = "mur@gmail.com"
        const val CORRECT_PASSWORD = "Abc123"
    }

    private lateinit var loginViewModel: LoginViewModel
    @MockK
    private lateinit var mockUserRepository: UserRepository
    @MockK
    private lateinit var mockFirebaseManager: FirebaseManager
    @MockK
    private var mockLoginListener: LoginListener = mockk(relaxed = true)
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
        loginViewModel =
            LoginViewModel(mockUserRepository, Schedulers.trampoline(), Schedulers.trampoline())
        loginViewModel.loginListener = mockLoginListener
        loginViewModel.email = CORRECT_EMAIL
        loginViewModel.password = CORRECT_PASSWORD
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
    fun `onClickTextViewRegister should call onClickTextViewRegister`() {
        loginViewModel.onClickTextViewRegister()
        verify(exactly = 1) { mockLoginListener.onClickTextViewRegister() }
    }

    @Test
    fun `onClickTextViewForgotPassword should call onClickTextViewForgotPassword`() {
        loginViewModel.onClickTextViewForgotPassword()
        verify(exactly = 1) { mockLoginListener.onClickTextViewForgotPassword() }
    }
    /////////////////////////////////////////////////////////////////////////////////////
    /**
     * Testing methods related to the rxjava2 library.
     * Events Tested: Click the "Log In" button
     */

    @Test
    fun `onClickButtonLogin should be isSuccessful`() {
        loginViewModel.onClickButtonLogin()

        every { mockAuthTask.isSuccessful } returns true
        slotCompleteListener.captured.onComplete(mockAuthTask)
        verify {
            mockAuth.signInWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify(exactly = 1) { mockLoginListener.resetTextInputLayout() }
        verify(exactly = 1) { mockLoginListener.showProgress() }
        verify(exactly = 1) { mockLoginListener.hideProgress() }
        verify(exactly = 1) { mockLoginListener.onSuccessAuth() }
    }

    @Test
    fun `onClickButtonLogin should be return error with FirebaseAuthException`() {
        loginViewModel.onClickButtonLogin()

        slotFailureListener.captured.onFailure(
            FirebaseAuthWeakPasswordException(
                AppConstants.ErrorCodeFirebaseAuth.ERROR_REQUIRES_RECENT_LOGIN,
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
        verify(exactly = 1) { mockLoginListener.onFailureAuth(R.string.error_requires_recent_login) }

    }

    @Test
    fun `onClickButtonLogin should be return unknown error with other Exception`() {
        loginViewModel.onClickButtonLogin()

        slotFailureListener.captured.onFailure(Exception())
        verify {
            mockAuth.signInWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify { mockLoginListener.onFailureAuth(R.string.error_unknown) }
    }

    @Test
    fun `onClickButtonLogin with invalid email should be call inEmailValidationError`() {
        loginViewModel.email = "adddss.com"

        loginViewModel.onClickButtonLogin()
        verify(exactly = 1) { mockLoginListener.inEmailValidationError(R.string.error_invalid_email_format) }
    }

    @Test
    fun `onClickButtonLogin with invalid password should be call inPasswordValidationError`() {
        loginViewModel.password = "123456"

        loginViewModel.onClickButtonLogin()
        verify(exactly = 1) { mockLoginListener.inPasswordValidationError(R.string.error_password_weak) }
    }
}