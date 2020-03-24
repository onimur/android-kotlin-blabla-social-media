/*
 *
 *  * Created by Murillo Comino on 26/10/19 13:56
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 13:56
 *
 */

package com.onimus.blablasocialmedia.mvvm.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.onimus.blablasocialmedia.mvvm.data.firebase.FirebaseManager

class UserRepository(private val firebase: FirebaseManager) {

    fun registerUser(email: String, password: String) =
        firebase.registerUser(email, password)

    fun logInUser(email: String, password: String) = firebase.logInUser(email, password)

    fun logInUser(credential: AuthCredential) = firebase.logInUser(credential)

    fun googleSignIn(task: Task<GoogleSignInAccount>) =
        firebase.googleSignInAccount(task)

    fun resetPassword(email: String) = firebase.resetPassword(email)

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()
}