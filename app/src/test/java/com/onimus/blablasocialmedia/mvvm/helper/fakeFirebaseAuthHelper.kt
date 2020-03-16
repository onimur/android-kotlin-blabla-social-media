/*
 *
 *  * Created by Murillo Comino on 16/03/20 12:19
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 16/03/20 12:19
 *
 */

package com.onimus.blablasocialmedia.mvvm.helper

import com.google.firebase.auth.FirebaseUser
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.EMAIL
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.NAME
import com.onimus.blablasocialmedia.mvvm.helper.TestConstants.Companion.UID
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK


class FakeUser {
    @MockK
    private lateinit var mockUser: FirebaseUser

    fun getMockFirebaseUser(): FirebaseUser {
        MockKAnnotations.init(this)

        every { mockUser.uid } returns UID
        every { mockUser.email } returns EMAIL
        every { mockUser.displayName } returns NAME

        return mockUser
    }
}