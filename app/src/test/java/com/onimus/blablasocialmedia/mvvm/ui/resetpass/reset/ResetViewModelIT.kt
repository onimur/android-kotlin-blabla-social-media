/*
 *
 *  * Created by Murillo Comino on 02/03/20 20:44
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 20/02/20 21:20
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.resetpass.reset

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
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
import org.junit.Rule
import org.junit.Test


class ResetViewModelIT {


    companion object {
        const val CORRECT_EMAIL = "mur@gmail.com"
        const val CORRECT_PASSWORD = "Abc123"
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var resetViewModel: ResetViewModel
    @MockK
    private lateinit var mockUserRepository: UserRepository
    @MockK
    private lateinit var mockFirebaseManager: FirebaseManager
    @MockK
    private var mockResetListener: ResetListener = mockk(relaxed = true)
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
        resetViewModel =
            ResetViewModel(mockUserRepository, Schedulers.trampoline(), Schedulers.trampoline())
        resetViewModel.resetListener = mockResetListener
        resetViewModel.email = CORRECT_EMAIL
        resetViewModel.password = CORRECT_PASSWORD
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

    /**
     * Testing methods related to the rxjava2 library.
     * Events Tested: Click the "Reset Password" button
     */
    @Test
    fun `onClickButtonReset should be isSuccessful`() {
        resetViewModel.onClickButtonReset()

        every { mockVoidTask.isSuccessful } returns true
        slotCompleteListenerReset.captured.onComplete(mockVoidTask)
        verify {
            mockAuth.sendPasswordResetEmail(
                eq(CORRECT_EMAIL)
            )
        }
        verify(exactly = 2) { mockResetListener.resetTextInputLayout() }
        verify(exactly = 1) { mockResetListener.showProgress() }
        verify(exactly = 1) { mockResetListener.hideProgress() }
        verify(exactly = 1) { mockResetListener.onSuccessAuth() }
    }

    @Test
    fun `onClickButtonReset should be return error with FirebaseAuthException`() {
        resetViewModel.onClickButtonReset()

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
        verify(exactly = 1) { mockResetListener.onFailureAuth(R.string.error_user_not_found) }

    }

    @Test
    fun `onClickButtonReset should be return unknown error with other Exception`() {
        resetViewModel.onClickButtonReset()

        slotFailureListenerReset.captured.onFailure(Exception())
        verify {
            mockAuth.sendPasswordResetEmail(
                eq(CORRECT_EMAIL)
            )
        }
        verify { mockResetListener.onFailureAuth(R.string.error_unknown) }
    }

    @Test
    fun `onClickButtonReset with invalid email should be call inEmailValidationError`() {
        resetViewModel.email = "adddss.com"

        resetViewModel.onClickButtonReset()
        verify(exactly = 1) { mockResetListener.inEmailValidationError(R.string.error_invalid_email_format) }
    }
}