/*
 *
 *  * Created by Murillo Comino on 26/10/19 16:36
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 16:36
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.main

import androidx.lifecycle.ViewModel
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository

class MainViewModel (private val repository: UserRepository) : ViewModel() {

    var mainListener: MainListener? = null

    private val user by lazy {
        repository.currentUser
    }

    fun checkUserStatus() {
        if (user != null) {
            mainListener?.onUserLogged()
        }
    }

    fun onClickButtonLogin() {
        mainListener?.onLoginClicked()
    }

    fun onClickButtonRegister() {
        mainListener?.onRegisterClicked()

    }
}
