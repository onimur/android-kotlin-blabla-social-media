/*
 *
 *  * Created by Murillo Comino on 27/02/20 12:47
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 27/02/20 12:47
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.main

import com.google.firebase.auth.FirebaseAuth
import com.onimus.blablasocialmedia.mvvm.data.firebase.FirebaseManager
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainViewModelIT {

    private lateinit var mainViewModel: MainViewModel
    @MockK
    private lateinit var mockUserRepository: UserRepository
    @MockK
    private lateinit var mockFirebaseManager: FirebaseManager
    @MockK
    private var mockMainListener: MainListener = mockk(relaxed = true)
    @MockK
    private lateinit var mockAuth: FirebaseAuth

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)

        setupMocks()

    }

    private fun setupMocks() {
        mockAuth = FirebaseAuth.getInstance()
        mockFirebaseManager = FirebaseManager()
        mockUserRepository = UserRepository(mockFirebaseManager)
        mainViewModel =
            MainViewModel(mockUserRepository)
        mainViewModel.mainListener = mockMainListener

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `checkUserStatus with valid user should call the onUserLogged`() {
        mainViewModel.checkUserStatus()
        verify(exactly = 1) { mockMainListener.onUserLogged() }
    }

    @Test
    fun `checkUserStatus with invalid user should not call the onUserLogged`() {
        every { mockAuth.currentUser } returns null

        mainViewModel.checkUserStatus()
        verify(exactly = 0) { mockMainListener.onUserLogged() }
    }

    @Test
    fun `onClickButtonLogin is clicked should call the onLoginClicked`() {

        mainViewModel.onClickButtonLogin()
        verify(exactly = 1) { mockMainListener.onLoginClicked() }
    }

    @Test
    fun `onClickButtonRegister is clicked should call the onRegisterClicked`() {

        mainViewModel.onClickButtonRegister()
        verify(exactly = 1) { mockMainListener.onRegisterClicked() }
    }
}