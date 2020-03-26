/*
 *
 *  * Created by Murillo Comino on 02/03/20 15:50
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 02/03/20 15:38
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.login


import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.onimus.blablasocialmedia.mvvm.common.ProgressViewModel
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import com.onimus.blablasocialmedia.mvvm.exception.EmailException
import com.onimus.blablasocialmedia.mvvm.exception.PasswordException
import com.onimus.blablasocialmedia.mvvm.utils.HandleErrors
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class LoginViewModel(
    private val repository: UserRepository,
    private val processScheduler: Scheduler = Schedulers.io(),
    private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ProgressViewModel() {

    var email: String? = null
    var password: String? = null
    var loginListener: LoginListener? = null

    private val handleErrors = HandleErrors()

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    /**
     * button handler
     */
    fun onClickButtonLogin() {
        setActionToAuthenticationButton()
    }

    fun onClickButtonGoogleSignIn(task: Task<GoogleSignInAccount>) {
        //login process
        setActionToGoogleSignIn(task)
    }

    fun firebaseAuthWithGoogle(credential: AuthCredential) {
        //login process
        setActionToAuthenticationGoogleSignIn(credential)
    }

    fun onClickTextViewRegister() {
        //go to fragment_register
        loginListener?.onClickTextViewRegister()
    }

    fun onClickTextViewForgotPassword() {
        //go to fragment_reset_password
        loginListener?.onClickTextViewForgotPassword()
    }

    /**
     * Action for buttons
     */
    private fun setActionToAuthenticationButton() {
        loginListener?.resetTextInputLayout()

        loginListener?.showProgress()
        //calling repository to perform the actual authentication
        disposables.add(getDisposableOnAuth(repository.logInUser(email, password)))
    }

    private fun setActionToGoogleSignIn(task: Task<GoogleSignInAccount>) {
        //calling the repository to retrieve user information on the google account.
        disposables.add(getDisposableOnGoogleSignIn(repository.googleSignIn(task)))
    }

    private fun setActionToAuthenticationGoogleSignIn(credential: AuthCredential) {
        //if is valid then show progress
        loginListener?.showProgress()
        //calling repository to perform the actual authentication
        disposables.add(getDisposableOnAuth(repository.logInUser(credential)))
    }


    private fun getDisposableOnAuth(completable: Completable): Disposable {
        return completable
            .subscribeOn(processScheduler)
            .observeOn(observerScheduler)
            .subscribe({
                loginListener?.hideProgress()
                loginListener?.onSuccessAuth()
            }, {

                loginListener?.hideProgress()

                val error = handleErrors.getMessageError(it)

                when (it) {
                    //Show message in text input
                    is EmailException -> loginListener?.inEmailValidationError(error)
                    //Show message in text input
                    is PasswordException -> loginListener?.inPasswordValidationError(error)
                    //show message
                    else -> loginListener?.onFailureAuth(error)
                }
            })
    }

    private fun getDisposableOnGoogleSignIn(completable: Completable): Disposable {
        return completable
            .subscribeOn(processScheduler)
            .observeOn(observerScheduler)
            .subscribe({
                loginListener?.onSuccessGoogleSign()
            }, {
                val error = handleErrors.getMessageError(it)
                //show message
                loginListener?.onFailureAuth(error)
            })
    }

    override fun onCleared() {
        //onCleared is called when the app is put into the background and the app process is killed in order to free up the system's memory.
        super.onCleared()
        // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
        disposables.dispose()
    }
}
