/*
 *
 *  * Created by Murillo Comino on 26/10/19 18:41
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 18:41
 *
 */

package com.onimus.blablasocialmedia.mvvm.commons

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import androidx.lifecycle.MutableLiveData
import com.onimus.blablasocialmedia.mvvm.utils.HandleErrors
import io.reactivex.Scheduler


class AuthViewModel(
    private val repository: UserRepository,
    private val processScheduler: Scheduler = Schedulers.io(),
    private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null

    private val handleErrors = HandleErrors()

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    //create livedata variable to save the progressbar(dialog) state
    private val progressBarActive = MutableLiveData<Boolean>(false)

    // get status from viewmodel
    fun getProgressBarStatus(): LiveData<Boolean> {
        return progressBarActive
    }

    // save status to viewmodel
    fun setProgressBarStatus(status: Boolean) {
        progressBarActive.postValue(status)
    }

    fun onClickButtonRegister() {
        authListener?.resetTextInputLayout()
        //validate email and password
        val checkEmail = handleErrors.checkEmail(email)
        val checkPassword = handleErrors.checkPassword(password)

        //email and password is valid
        when {
            checkEmail.and(checkPassword) == -1 -> {
                authListener?.resetTextInputLayout()
                //if is valid then show progress
                authListener?.showProgress()
                //calling onRegisterClicked from repository to perform the actual authentication
                val disposable = repository.onRegisterClicked(email!!, password!!)
                    .subscribeOn(processScheduler)
                    .observeOn(observerScheduler)
                    .subscribe({
                        authListener?.hideProgress()
                        authListener?.onSuccessAuth()
                    }, {

                        authListener?.hideProgress()
                        val error = handleErrors.getMessageError(it)
                        //show message
                        authListener?.onFailureAuth(error)
                    })
                disposables.add(disposable)
            }
            checkEmail != -1 -> authListener?.inEmailValidationError(checkEmail)
            checkPassword != -1 -> authListener?.inPasswordValidationError(checkPassword)
        }
    }

    fun onClickButtonLogin() {
        authListener?.resetTextInputLayout()
        //validate email and password
        val checkEmail = handleErrors.checkEmail(email)
        val checkPassword = handleErrors.checkPassword(password)

        //email and password is valid
        when {
            checkEmail.and(checkPassword) == -1 -> {
                authListener?.resetTextInputLayout()
                //if is valid then show progress
                authListener?.showProgress()
                //calling onLoginClicked from repository to perform the actual authentication
                val disposable = repository.onLoginClicked(email!!, password!!)
                    .subscribeOn(processScheduler)
                    .observeOn(observerScheduler)
                    .subscribe({
                        authListener?.hideProgress()
                        authListener?.onSuccessAuth()
                    }, {

                        authListener?.hideProgress()
                        val error = handleErrors.getMessageError(it)
                        //show message
                        authListener?.onFailureAuth(error)
                    })
                disposables.add(disposable)
            }
            checkEmail != -1 -> authListener?.inEmailValidationError(checkEmail)
            checkPassword != -1 -> authListener?.inPasswordValidationError(checkPassword)
        }
    }

    fun onClickTextViewLogin() {
        //go to login_fragment
        authListener?.onNavigate()
    }

    fun onClickTextViewRegister() {
        //go to register_fragment
        authListener?.onNavigate()
    }
}