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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.onimus.blablasocialmedia.mvvm.helper.MockKHelper
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.ERROR_MESSAGE
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.mockito.ArgumentMatchers.anyString

class FirebaseManagerIT {

    companion object {
        @AfterClass @JvmStatic
        fun tearDownClass() {
            unmockkAll()
        }
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var mockAuth: FirebaseAuth
    @MockK
    private var mockGoogleTasks: Task<GoogleSignInAccount> = mockk(relaxed = true)
    private lateinit var mockKAuthResult: MockKHelper<AuthResult>
    private lateinit var mockKVoid: MockKHelper<Void>

    private lateinit var credential: AuthCredential
    private lateinit var firebaseManager: FirebaseManager

    private fun initAnnotation() {
        MockKAnnotations.init(this)
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
    }

    private fun initVariables() {
        mockAuth = FirebaseAuth.getInstance()
        credential = GoogleAuthProvider.getCredential(anyString(), null)
        firebaseManager = FirebaseManager()
    }

    private fun setupMockVoid() {
        mockKVoid = MockKHelper()
        mockKVoid.initializeMockKs({mockAuth.sendPasswordResetEmail(EMAIL)})

        //observer
        mockKVoid.initializeCapture()
    }

    private fun setupMockAuthResult() {
        mockKAuthResult = MockKHelper()
        mockKAuthResult
            .initializeMockKs({ mockAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD) },
                { mockAuth.signInWithEmailAndPassword(EMAIL, PASSWORD) },
                { mockAuth.signInWithCredential(credential) })

        //observer
        mockKAuthResult.initializeCapture()
    }

    @Before
    fun setUp() {
        initAnnotation()
        initVariables()
        setupMockAuthResult()
        setupMockVoid()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `registerUser should be isSuccessful`() {
        mockKAuthResult.taskSuccessful()
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockKAuthResult.slotCaptured()
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
        mockKAuthResult.taskSuccessful(false)
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockKAuthResult.slotCaptured()
        with(register) {
            assertEmpty()
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `registerUser should be return error with FirebaseAuthException`() {
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockKAuthResult
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
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `registerUser should be return Exception`() {
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockKAuthResult
            .slotCaptured(GoogleAuthException(ERROR_MESSAGE))

        with(register) {
            assertSubscribed()
            assertFailureAndMessage(Exception::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `loginUser with Email and Password should be isSuccessful`() {
        mockKAuthResult.taskSuccessful()
        val login = firebaseManager.logInUser(EMAIL, PASSWORD).test()

        //check capture
        mockKAuthResult.slotCaptured()
        with(login) {
            assertSubscribed()
            assertComplete()
            assertNoErrors()
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `loginUser with Email and Password with task it's unsuccessful should be empty`() {
        mockKAuthResult.taskSuccessful(false)
        val login = firebaseManager.logInUser(EMAIL, PASSWORD).test()

        //check capture
        mockKAuthResult.slotCaptured()
        with(login) {
            assertEmpty()
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `loginUser with Email and Password should be return error with FirebaseAuthException`() {
        val login = firebaseManager.logInUser(EMAIL, PASSWORD).test()

        //check capture
        mockKAuthResult
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
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `loginUser with Email and Password should be return Exception`() {
        val login = firebaseManager.logInUser(EMAIL, PASSWORD).test()

        //check capture
        mockKAuthResult
            .slotCaptured(GoogleAuthException(ERROR_MESSAGE))

        with(login) {
            assertSubscribed()
            assertFailureAndMessage(Exception::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `loginUser with idToken should be isSuccessful`() {
        mockKAuthResult.taskSuccessful()
        val login = firebaseManager.logInUser(credential).test()

        //check capture
        mockKAuthResult.slotCaptured()
        with(login) {
            assertSubscribed()
            assertComplete()
            assertNoErrors()
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `loginUser with idToken with task it's unsuccessful should be empty`() {
        mockKAuthResult.taskSuccessful(false)
        val login = firebaseManager.logInUser(credential).test()

        //check capture
        mockKAuthResult.slotCaptured()
        with(login) {
            assertEmpty()
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `loginUser with idToken should be return error with FirebaseAuthException`() {
        val login = firebaseManager.logInUser(credential).test()

        //check capture
        mockKAuthResult
            .slotCaptured(FirebaseAuthWebException(anyString(), ERROR_MESSAGE))

        with(login) {
            assertSubscribed()
            assertFailureAndMessage(FirebaseAuthException::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `loginUser with idToken should be return Exception`() {
        val login = firebaseManager.logInUser(credential).test()

        //check capture
        mockKAuthResult
            .slotCaptured(GoogleAuthException(ERROR_MESSAGE))

        with(login) {
            assertSubscribed()
            assertFailureAndMessage(Exception::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `googleSignInAccount with valid Credential should complete the task `() {
        val googleSignIn = firebaseManager.googleSignInAccount(mockGoogleTasks).test()

        with(googleSignIn) {
            assertSubscribed()
            assertComplete()
            assertNoErrors()
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `googleSignInAccount with GoogleAuthException should be return Exception`() {
        every { mockGoogleTasks.result } throws GoogleAuthException(ERROR_MESSAGE)
        val googleSignIn = firebaseManager.googleSignInAccount(mockGoogleTasks).test()

        with(googleSignIn) {
            assertSubscribed()
            assertFailureAndMessage(Exception::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `resetPassword should be isSuccessful`() {
        mockKVoid.taskSuccessful()
        val reset = firebaseManager.resetPassword(EMAIL).test()

        //check capture
        mockKVoid.slotCaptured()
        with(reset) {
            assertSubscribed()
            assertComplete()
            assertNoErrors()
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `resetPassword with task it's unsuccessful should be empty`() {
        mockKVoid.taskSuccessful(false)
        val reset = firebaseManager.resetPassword(EMAIL).test()

        //check capture
        mockKVoid.slotCaptured()
        with(reset) {
            assertEmpty()
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `resetPassword should be return error with FirebaseAuthException`() {
        val reset = firebaseManager.resetPassword(EMAIL).test()

        //check capture
        mockKVoid
            .slotCaptured(
                FirebaseAuthWeakPasswordException(
                    anyString(),
                    ERROR_MESSAGE,
                    anyString()
                )
            )

        with(reset) {
            assertSubscribed()
            assertFailureAndMessage(FirebaseAuthException::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }

    @Test
    fun `resetPassword should be return Exception`() {
        val reset = firebaseManager.resetPassword(EMAIL).test()

        //check capture
        mockKVoid
            .slotCaptured(GoogleAuthException(ERROR_MESSAGE))

        with(reset) {
            assertSubscribed()
            assertFailureAndMessage(Exception::class.java, ERROR_MESSAGE)
            assertFalse(isDisposed)
            dispose()
            assertTrue(isDisposed)
        }
    }
}