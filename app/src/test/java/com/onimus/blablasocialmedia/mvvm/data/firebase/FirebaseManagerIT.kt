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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirebaseManagerIT {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var firebaseManager: FirebaseManager

    @MockK
    private lateinit var mockAuth: FirebaseAuth

    @MockK
    var mockAuthTask: Task<AuthResult> = mockk(relaxed = true)

    private val slotCompleteListener = slot<OnCompleteListener<AuthResult>>()

    private fun setupMocks() {
        mockAuth = FirebaseAuth.getInstance()
        firebaseManager = FirebaseManager()

        every {
            mockAuth.createUserWithEmailAndPassword(
                EMAIL,
                PASSWORD
            )
        } returns mockAuthTask


        every { mockAuthTask.addOnCompleteListener(capture(slotCompleteListener)) } returns mockAuthTask

    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)

        setupMocks()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun registerUser() {
        every { mockAuthTask.isSuccessful } returns true
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()


        slotCompleteListener.captured.onComplete(mockAuthTask)
        with(register) {
            hasSubscription()
            assertComplete()
            assertNoErrors()
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