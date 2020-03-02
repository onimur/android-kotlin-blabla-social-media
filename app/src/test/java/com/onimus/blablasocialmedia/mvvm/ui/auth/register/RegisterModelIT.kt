/*
 *
 *  * Created by Murillo Comino on 02/03/20 20:39
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 20/02/20 21:20
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class RegisterModelIT {


    companion object {
        const val CORRECT_EMAIL = "mur@gmail.com"
        const val CORRECT_PASSWORD = "Abc123"
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var registerViewModel: RegisterViewModel
    @MockK
    private lateinit var mockUserRepository: UserRepository
    @MockK
    private lateinit var mockFirebaseManager: FirebaseManager
    @MockK
    private var mockRegisterListener: RegisterListener = mockk(relaxed = true)
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
        registerViewModel =
            RegisterViewModel(mockUserRepository, Schedulers.trampoline(), Schedulers.trampoline())
        registerViewModel.registerListener = mockRegisterListener
        registerViewModel.email = CORRECT_EMAIL
        registerViewModel.password = CORRECT_PASSWORD
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
        assertFalse(registerViewModel.getProgressBarStatus().value!!)
    }

    @Test
    fun `getProgressBarStatus should return true`() {
        registerViewModel.setProgressBarStatus(true)
        assertTrue(registerViewModel.getProgressBarStatus().value!!)
    }

    @Test
    fun `onClickTextViewLogin should call onNavigate`() {
        registerViewModel.onClickTextViewLogin()
        verify(exactly = 1) { mockRegisterListener.onNavigate() }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    /**
     * Testing methods related to the rxjava2 library.
     * Events Tested: Click the "Register" button and click the "Log In" button
     */
    @Test
    fun `onClickButtonRegister should be isSuccessful`() {
        registerViewModel.onClickButtonRegister()

        every { mockAuthTask.isSuccessful } returns true
        slotCompleteListener.captured.onComplete(mockAuthTask)
        verify {
            mockAuth.createUserWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify(exactly = 2) { mockRegisterListener.resetTextInputLayout() }
        verify(exactly = 1) { mockRegisterListener.showProgress() }
        verify(exactly = 1) { mockRegisterListener.hideProgress() }
        verify(exactly = 1) { mockRegisterListener.onSuccessAuth() }
    }

    @Test
    fun `onClickButtonRegister should be return error with FirebaseAuthException`() {
        registerViewModel.onClickButtonRegister()

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
        verify(exactly = 1) { mockRegisterListener.onFailureAuth(R.string.error_requires_recent_login) }

    }

    @Test
    fun `onClickButtonRegister should be return unknown error with other Exception`() {
        registerViewModel.onClickButtonRegister()

        slotFailureListener.captured.onFailure(Exception())
        verify {
            mockAuth.createUserWithEmailAndPassword(
                eq(CORRECT_EMAIL),
                eq(CORRECT_PASSWORD)
            )
        }
        verify { mockRegisterListener.onFailureAuth(R.string.error_unknown) }
    }

    @Test
    fun `onClickButtonRegister with invalid email should be call inEmailValidationError`() {
        registerViewModel.email = "adddss.com"

        registerViewModel.onClickButtonRegister()
        verify(exactly = 1) { mockRegisterListener.inEmailValidationError(R.string.error_invalid_email_format) }
    }

    @Test
    fun `onClickButtonRegister with invalid password should be call inPasswordValidationError`() {
        registerViewModel.password = "123456"

        registerViewModel.onClickButtonRegister()
        verify(exactly = 1) { mockRegisterListener.inPasswordValidationError(R.string.error_password_weak) }
    }
}