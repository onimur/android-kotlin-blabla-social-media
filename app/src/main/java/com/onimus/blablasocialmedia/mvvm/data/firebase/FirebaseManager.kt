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
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants
import io.reactivex.Completable
import io.reactivex.CompletableEmitter

class FirebaseManager {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun onRegisterClicked(email: String, password: String) = Completable.create { emitter ->
        if (!emitter.isDisposed) {
            createOrSigInUser(firebaseAuth.createUserWithEmailAndPassword(email, password), emitter)
        }
    }

    fun onLoginClicked(email: String, password: String) = Completable.create { emitter ->
        if (!emitter.isDisposed) {
            createOrSigInUser(firebaseAuth.signInWithEmailAndPassword(email, password), emitter)
        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    private fun createOrSigInUser(
        nameTask: Task<AuthResult>,
        emitter: CompletableEmitter
    ) {

        nameTask.addOnCompleteListener {
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