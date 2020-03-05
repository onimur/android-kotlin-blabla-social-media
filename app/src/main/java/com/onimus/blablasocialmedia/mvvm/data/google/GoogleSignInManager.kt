/*
 *
 *  * Created by Murillo Comino on 04/03/20 19:52
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 04/03/20 19:52
 *
 */

package com.onimus.blablasocialmedia.mvvm.data.google

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.onimus.blablasocialmedia.R

class GoogleSignInManager(private val context: Context) {
    fun getSignedInAccountFromIntent(data: Intent?): Task<GoogleSignInAccount> {
        return GoogleSignIn.getSignedInAccountFromIntent(data)
    }

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(context, gso)
    }

    // Configure Google Sign In
    private val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    val signInIntent = googleSignInClient.signInIntent

}



