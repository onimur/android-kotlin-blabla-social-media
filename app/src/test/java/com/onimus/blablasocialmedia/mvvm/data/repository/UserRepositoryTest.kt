/*
 *
 *  * Created by Murillo Comino on 23/03/20 15:33
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 23/03/20 15:33
 *
 */

package com.onimus.blablasocialmedia.mvvm.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.onimus.blablasocialmedia.mvvm.data.firebase.FirebaseManager
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.PASSWORD
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class UserRepositoryTest {

    companion object {
        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            unmockkAll()
        }
    }

    @MockK
    private var mockGoogleTasks: Task<GoogleSignInAccount> = mockk(relaxed = true)

    private lateinit var credential: AuthCredential

    @MockK
    private var mockFirebaseManager: FirebaseManager = mockk(relaxed = true)

    private lateinit var userRepository: UserRepository
    private lateinit var completable: Completable
    private lateinit var mockCompletable: () -> Completable

    private fun initAnnotation() {
        MockKAnnotations.init(this)
    }

    private fun initVariables() {
        credential = GoogleAuthProvider.getCredential(anyString(), null)
        userRepository = UserRepository(mockFirebaseManager)
    }

    @Before
    fun setUp() {
        initAnnotation()
        initVariables()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun verifyCompletable() {
        verify(exactly = 1) { mockCompletable.invoke() }
        assertThat(completable, `is`(mockCompletable.invoke()))
    }

    @Test
    fun registerUser() {
        completable = userRepository.registerUser(EMAIL, PASSWORD)
        mockCompletable = { mockFirebaseManager.registerUser(EMAIL, PASSWORD) }
        verifyCompletable()
    }

    @Test
    fun `logInUser with Email and Password `() {
        completable = userRepository.logInUser(EMAIL, PASSWORD)
        mockCompletable = { mockFirebaseManager.logInUser(EMAIL, PASSWORD) }
        verifyCompletable()
    }

    @Test
    fun `logInUser with Credential`() {
        completable = userRepository.logInUser(credential)
        mockCompletable = { mockFirebaseManager.logInUser(credential) }
        verifyCompletable()
    }

    @Test
    fun googleSignIn() {
        completable = userRepository.googleSignIn(mockGoogleTasks)
        mockCompletable = { mockFirebaseManager.googleSignInAccount(mockGoogleTasks) }
        verifyCompletable()
    }

    @Test
    fun resetPassword() {
        completable = userRepository.resetPassword(EMAIL)
        mockCompletable = { mockFirebaseManager.resetPassword(EMAIL) }
        verifyCompletable()
    }
}