/*
 *
 *  * Created by Murillo Comino on 26/10/19 14:03
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 14:03
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.profile

import androidx.lifecycle.ViewModel
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    var profileListener: ProfileListener? = null

    val user by lazy {
        repository.currentUser
    }

    fun checkUserStatus() {
        if (user == null) {
            //user signout
            profileListener?.onLogout()
        }
    }

    fun onClickButtonLogout(){
        repository.logout()
        profileListener?.onLogout()

    }
}
