/*
 *
 *  * Created by Murillo Comino on 02/03/20 15:50
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 26/10/19 18:51
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }

}