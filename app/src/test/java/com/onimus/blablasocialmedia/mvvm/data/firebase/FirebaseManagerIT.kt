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
import com.google.firebase.auth.*
import com.onimus.blablasocialmedia.mvvm.helper.MockkHelper
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.ERROR_MESSAGE
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class FirebaseManagerIT {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var mockAuth: FirebaseAuth

    private lateinit var credential: AuthCredential

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var mockkHelper: MockkHelper<AuthResult>

    private fun setupMocks() {
        mockkHelper = MockkHelper()
        mockAuth = FirebaseAuth.getInstance()
        credential = GoogleAuthProvider.getCredential(anyString(), null)
        firebaseManager = FirebaseManager()

        mockkHelper
            .initializeMockks({ mockAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD) },
                { mockAuth.signInWithEmailAndPassword(EMAIL, PASSWORD) },
                { mockAuth.signInWithCredential(credential) })

        //observer
        mockkHelper.initializeCapture()
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
        mockkHelper.taskSuccessful()
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockkHelper.slotCaptured()
        with(register) {
            assertSubscribed()
            assertComplete()
            assertNoErrors()
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `registerUser with task it's unsuccessful should be empty`() {
        mockkHelper.taskSuccessful(false)
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockkHelper.slotCaptured()
        with(register) {
            assertEmpty()
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `registerUser should be return error with FirebaseAuthException`() {
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockkHelper
            .slotCaptured(
                FirebaseAuthWeakPasswordException(
                    anyString(),
                    ERROR_MESSAGE,
                    anyString()
                )
            )

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
        mockkHelper
            .slotCaptured(GoogleAuthException(ERROR_MESSAGE))

        with(register) {
            assertSubscribed()
            assertFailureAndMessage(Exception::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `loginUser with Email and Password should be isSuccessful`() {
        mockkHelper.taskSuccessful()
        val login = firebaseManager.logInUser(EMAIL, PASSWORD).test()

        //check capture
        mockkHelper.slotCaptured()
        with(login) {
            assertSubscribed()
            assertComplete()
            assertNoErrors()
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `loginUser with Email and Password with task it's unsuccessful should be empty`() {
        mockkHelper.taskSuccessful(false)
        val login = firebaseManager.logInUser(EMAIL, PASSWORD).test()

        //check capture
        mockkHelper.slotCaptured()
        with(login) {
            assertEmpty()
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `loginUser with Email and Password should be return error with FirebaseAuthException`() {
        val login = firebaseManager.logInUser(EMAIL, PASSWORD).test()

        //check capture
        mockkHelper
            .slotCaptured(
                FirebaseAuthWeakPasswordException(
                    anyString(),
                    ERROR_MESSAGE,
                    anyString()
                )
            )

        with(login) {
            assertSubscribed()
            assertFailureAndMessage(FirebaseAuthException::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `loginUser with Email and Password should be return Exception`() {
        val login = firebaseManager.logInUser(EMAIL, PASSWORD).test()

        //check capture
        mockkHelper
            .slotCaptured(GoogleAuthException(ERROR_MESSAGE))

        with(login) {
            assertSubscribed()
            assertFailureAndMessage(Exception::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `loginUser with idToken should be isSuccessful`() {
        mockkHelper.taskSuccessful()
        val login = firebaseManager.logInUser(credential).test()

        //check capture
        mockkHelper.slotCaptured()
        with(login) {
            assertSubscribed()
            assertComplete()
            assertNoErrors()
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `loginUser with idToken with task it's unsuccessful should be empty`() {
        mockkHelper.taskSuccessful(false)
        val login = firebaseManager.logInUser(credential).test()

        //check capture
        mockkHelper.slotCaptured()
        with(login) {
            assertEmpty()
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `loginUser with idToken should be return error with FirebaseAuthException`() {
        val login = firebaseManager.logInUser(credential).test()

        //check capture
        mockkHelper
            .slotCaptured(FirebaseAuthWebException(anyString(), ERROR_MESSAGE))

        with(login) {
            assertSubscribed()
            assertFailureAndMessage(FirebaseAuthException::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
        }
    }

    @Test
    fun `loginUser with idToken should be return Exception`() {
        val login = firebaseManager.logInUser(credential).test()

        //check capture
        mockkHelper
            .slotCaptured(GoogleAuthException(ERROR_MESSAGE))

        with(login) {
            assertSubscribed()
            assertFailureAndMessage(Exception::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
        }
    }

    @Test
    fun googleSignInAccount() {
    }

    @Test
    fun resetPassword() {
    }
}