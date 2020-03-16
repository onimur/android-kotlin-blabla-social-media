/*
 *
 *  * Created by Murillo Comino on 10/03/20 14:54
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/03/20 14:54
 *
 */

package com.onimus.blablasocialmedia.mvvm.data.firebase

import androidx.test.filters.LargeTest
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseUser
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is

import org.junit.Assert.*

@LargeTest
class FirebaseManagerTest {

    companion object {
        const val EMAIL_USER = "abc@gmail.com"
        const val PASSWORD_USER = "Abc123"
    }

    private lateinit var firebaseManager: FirebaseManager
    private var firebaseUser: FirebaseUser? = null

    private fun newUser() {
        firebaseManager.registerUser(EMAIL_USER, PASSWORD_USER).blockingAwait()
        firebaseUser = firebaseManager.currentUser()
    }

    private fun signInUser() {
        firebaseManager.logInUser(EMAIL_USER, PASSWORD_USER).blockingAwait()
        firebaseUser = firebaseManager.currentUser()
    }

    private fun signOutUser() {
        firebaseManager.logout()
        firebaseUser = firebaseManager.currentUser()
        assertThat(firebaseUser, Is(nullValue()))
    }

    private fun deleteUser() {
        Tasks.await(firebaseUser!!.delete())
    }

    @Before
    fun setUp() {
        firebaseManager = FirebaseManager()
    }

    @After
    fun tearDown() {
        deleteUser()
    }

    @Test
    fun onRegisterClicked() {
        newUser()
        assertThat(firebaseUser, Is(notNullValue()))
        assertThat(firebaseUser!!.email, Is(EMAIL_USER))

    }

    @Test
    fun onLoginClicked() {
        newUser()
        signOutUser()
        signInUser()

        assertThat(firebaseUser, Is(notNullValue()))
        assertThat(firebaseUser!!.email, Is(EMAIL_USER))

    }

    @Test
    fun onGoogleSignInClicked() {
    }

    @Test
    fun firebaseAuthWithGoogle() {
    }

    @Test
    fun onResetPasswordClicked() {
    }
}