/*
 *
 *  * Created by Murillo Comino on 11/03/20 11:23
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/03/20 11:23
 *
 */

package com.onimus.blablasocialmedia.mvvm.data.firebase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.auth.GoogleAuthException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.onimus.blablasocialmedia.mvvm.helper.MockkTasksHelper
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.ERROR_MESSAGE
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class FirebaseManagerIT {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var mockAuth: FirebaseAuth

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var mockkTasksHelper: MockkTasksHelper<AuthResult>

    private fun setupMocks() {
        mockkTasksHelper = MockkTasksHelper()
        mockAuth = FirebaseAuth.getInstance()
        firebaseManager = FirebaseManager()

        mockkTasksHelper.initializeMockks({
            mockAuth.createUserWithEmailAndPassword(
                EMAIL,
                PASSWORD
            )
        })

        //observer
        mockkTasksHelper.initializeCapture()
    }

    @Before
    fun setUp() {
        setupMocks()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `registerUser should be isSuccessful`() {
        mockkTasksHelper.taskSuccessful()
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockkTasksHelper.slotCaptured()
        with(register) {
            assertSubscribed()
            assertComplete()
            assertNoErrors()
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `registerUser with task it's unsuccessful should be empty`() {
        mockkTasksHelper.taskSuccessful(false)
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockkTasksHelper.slotCaptured()
        with(register) {
            assertEmpty()
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `registerUser should be return error with FirebaseAuthException`() {
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockkTasksHelper
            .slotCaptured(FirebaseAuthWeakPasswordException(anyString(), ERROR_MESSAGE, anyString()))

        with(register) {
            assertSubscribed()
            assertFailureAndMessage(FirebaseAuthException::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `registerUser should be return Exception`() {
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockkTasksHelper
            .slotCaptured(GoogleAuthException(ERROR_MESSAGE))

        with(register) {
            assertSubscribed()
            assertFailureAndMessage(Exception::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
        }
    }

    @Test
    fun logInUser() {
    }

    @Test
    fun testLogInUser() {
    }

    @Test
    fun googleSignInAccount() {
    }

    @Test
    fun resetPassword() {
    }
}