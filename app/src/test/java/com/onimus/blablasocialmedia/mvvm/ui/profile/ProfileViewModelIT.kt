/*
 *
 *  * Created by Murillo Comino on 02/03/20 11:02
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 27/02/20 15:10
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.profile

import com.google.firebase.auth.FirebaseAuth
import com.onimus.blablasocialmedia.mvvm.data.firebase.FirebaseManager
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProfileViewModelIT {

    private lateinit var profileViewModel: ProfileViewModel
    @MockK
    private lateinit var mockUserRepository: UserRepository
    @MockK
    private lateinit var mockFirebaseManager: FirebaseManager
    @MockK
    private var mockProfileListener: ProfileListener = mockk(relaxed = true)
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
        profileViewModel =
            ProfileViewModel(mockUserRepository)
        profileViewModel.profileListener = mockProfileListener

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `checkUserStatus with empty user should call the onLogout`() {
        every { mockAuth.currentUser } returns null

        profileViewModel.checkUserStatus()
        verify(exactly = 1) { mockProfileListener.onLogout() }
    }

    @Test
    fun `checkUserStatus with valid user should not call the onLogout`() {
        profileViewModel.checkUserStatus()
        verify(exactly = 0) { mockProfileListener.onLogout() }
    }

    @Test
    fun `onClickButtonLogout is clicked should call the onLogout`() {

        profileViewModel.onClickButtonLogout()
        verify(exactly = 1) { mockProfileListener.onLogout() }
        verify (exactly = 1){ mockUserRepository.logout() }
    }
}