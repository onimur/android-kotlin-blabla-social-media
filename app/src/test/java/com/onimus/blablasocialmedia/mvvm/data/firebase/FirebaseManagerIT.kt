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
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.onimus.blablasocialmedia.mvvm.helper.MockkTasksHelper
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

        mockkTasksHelper.initializeMockks({mockAuth.createUserWithEmailAndPassword(
            EMAIL,
            PASSWORD
        )})

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
    fun registerUser() {
        mockkTasksHelper.taskSuccessful()
        val register = firebaseManager.registerUser(EMAIL, PASSWORD).test()

        //check capture
        mockkTasksHelper.slotCaptured()
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