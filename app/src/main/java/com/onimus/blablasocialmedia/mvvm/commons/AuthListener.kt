/*
 *
 *  * Created by Murillo Comino on 26/10/19 18:45
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 18:45
 *
 */

package com.onimus.blablasocialmedia.mvvm.commons

interface AuthListener {
    fun onSuccessAuth()
    fun onFailureAuth(resId: Int)
    fun onNavigate()
    fun inEmailValidationError(resId: Int)
    fun inPasswordValidationError(resId: Int)
    //
    fun showProgress()
    fun hideProgress()
    fun resetTextInputLayout()
}