/*
 *
 *  * Created by Murillo Comino on 26/10/19 13:54
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 13:54
 *
 */

package com.onimus.blablasocialmedia.mvvm.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants
import io.reactivex.Completable

class FirebaseManager {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun onRegisterClicked(email: String, password: String) = Completable.create { emitter ->
        if (!emitter.isDisposed) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(
                        AppConstants.Tag.LOG_D,
                        "UserWithEmail:success - ${it.result.toString()}"
                    )
                    emitter.onComplete()
                }
            }.addOnFailureListener {
                Log.w(AppConstants.Tag.LOG_W, "UserWithEmail:failure - ${it.message}", it.cause)
                emitter.onError(it)
            }
        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}