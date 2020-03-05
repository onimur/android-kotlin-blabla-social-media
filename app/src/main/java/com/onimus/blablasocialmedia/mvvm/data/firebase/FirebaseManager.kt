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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants
import io.reactivex.Completable
import io.reactivex.CompletableEmitter


class FirebaseManager {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun onRegisterClicked(email: String, password: String) = Completable.create { emitter ->
        if (!emitter.isDisposed) {
            actionFirebaseAuth(
                firebaseAuth.createUserWithEmailAndPassword(email, password),
                emitter
            )
        }
    }

    fun onLoginClicked(email: String, password: String) = Completable.create { emitter ->
        if (!emitter.isDisposed) {
            actionFirebaseAuth(firebaseAuth.signInWithEmailAndPassword(email, password), emitter)
        }
    }

    fun onGoogleSignInClicked(task: Task<GoogleSignInAccount>) = Completable.create { emitter ->
        if (!emitter.isDisposed) {
            getAccountToLogInFirebase(task, emitter)
        }
    }

    fun firebaseAuthWithGoogle(idToken: String?): Completable {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        return Completable.create { emitter ->
            if (!emitter.isDisposed) {
                actionFirebaseAuth(firebaseAuth.signInWithCredential(credential), emitter)
            }
        }
    }

    fun onResetPasswordClicked(email: String) = Completable.create { emitter ->
        if (!emitter.isDisposed) {
            actionFirebaseAuth(firebaseAuth.sendPasswordResetEmail(email), emitter)
        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    private fun <T : Any?> actionFirebaseAuth(
        nameTask: Task<T>,
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

    private fun <T : Any?> getAccountToLogInFirebase(
        nameTask: Task<T>,
        emitter: CompletableEmitter
    ) {
        try {
            nameTask.getResult(ApiException::class.java)
            emitter.onComplete()

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(AppConstants.Tag.LOG_W, "signInResult:failed code= ${e.statusCode}")
            emitter.onError(e)

        }
    }
}
