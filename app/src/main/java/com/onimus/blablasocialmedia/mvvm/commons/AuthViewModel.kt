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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants
import com.onimus.blablasocialmedia.mvvm.utils.HandleErrors
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


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

    /**
     * get status from viewmodel
     */
    fun getProgressBarStatus(): LiveData<Boolean> {
        return progressBarActive
    }

    /**
     *   save status to viewmodel
     */
    fun setProgressBarStatus(status: Boolean) {
        progressBarActive.postValue(status)
    }

    /**
     * button handler
     */
    fun onClickButtonRegister() {
        setActionToAuthenticationButton(AppConstants.Button.REGISTER)
    }

    fun onClickButtonLogin() {
        setActionToAuthenticationButton(AppConstants.Button.LOGIN)
    }

    fun onClickButtonReset() {
        setActionToResetButton()
    }

    fun onClickTextViewLogin() {
        //go to fragment_login
        authListener?.onNavigate()
    }

    fun onClickTextViewRegister() {
        //go to fragment_register
        authListener?.onClickTextViewRegister()
    }

    fun onClickTextViewForgotPassword() {
        //go to fragment_reset_password
        authListener?.onClickTextViewForgotPassword()
    }

    /**
     * Action for buttons
     */
    private fun setActionToAuthenticationButton(button: Int) {
        authListener?.resetTextInputLayout()
        //validate email and password
        val checkEmail = handleErrors.checkEmail(email)
        val checkPassword = handleErrors.checkPassword(password)

        //email and password is valid
        when {
            checkEmail.and(checkPassword) == AppConstants.VALID -> {
                authListener?.resetTextInputLayout()
                //if is valid then show progress
                authListener?.showProgress()
                //calling repository to perform the actual authentication
                val completable = when (button) {
                    AppConstants.Button.LOGIN -> repository.onLoginClicked(email!!, password!!)
                    else -> repository.onRegisterClicked(email!!, password!!)
                }

                val disposable = getDisposable(completable)
                disposables.add(disposable)
            }
            checkEmail != AppConstants.VALID -> authListener?.inEmailValidationError(checkEmail)
            checkPassword != AppConstants.VALID -> authListener?.inPasswordValidationError(
                checkPassword
            )
        }
    }

    private fun setActionToResetButton() {
        authListener?.resetTextInputLayout()
        //validate email and password

        //email is valid
        when (val checkEmail = handleErrors.checkEmail(email)) {
            AppConstants.VALID -> {
                authListener?.resetTextInputLayout()
                //if is valid then show progress
                authListener?.showProgress()
                //calling repository to perform the actual authentication
                val disposable = getDisposable(repository.onResetPasswordClicked(email!!))
                disposables.add(disposable)
            }
            else -> authListener?.inEmailValidationError(checkEmail)
        }
    }

    private fun getDisposable(completable: Completable): Disposable {
        return completable
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
    }

    override fun onCleared() {
        //onCleared is called when the app is put into the background and the app process is killed in order to free up the system's memory.
        super.onCleared()
        // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
        disposables.dispose()
    }
}
