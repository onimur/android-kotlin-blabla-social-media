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
import com.google.firebase.auth.FirebaseAuth
import com.onimus.blablasocialmedia.mvvm.data.firebase.FirebaseManager
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*

import org.junit.Assert.*


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

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        MockKAnnotations.init(this)
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)

        mockFirebaseManager = FirebaseManager()
        mockUserRepository = UserRepository(mockFirebaseManager)
        authViewModel =
            AuthViewModel(mockUserRepository, Schedulers.trampoline(), Schedulers.trampoline())
        authViewModel.authListener = mockAuthListener
        authViewModel.email = CORRECT_EMAIL
        authViewModel.password = CORRECT_PASSWORD

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
    fun `onClickTextViewRegister should call onNavigate`() {
        authViewModel.onClickTextViewRegister()
        verify(exactly = 1) { mockAuthListener.onNavigate() }
    }
}