/*
 *
 *  * Created by Murillo Comino on 26/03/20 11:17
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/03/20 23:27
 *
 */

package com.onimus.blablasocialmedia.mvvm.common

interface AuthenticationListener {
    fun onSuccessAuth()
    fun onFailureAuth(resId: Int)
    fun inEmailValidationError(resId: Int)
    fun inPasswordValidationError(resId: Int) {}
    //
    fun showProgress()
    fun hideProgress()
    fun resetTextInputLayout()
}