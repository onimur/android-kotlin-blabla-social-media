/*
 *
 *  * Created by Murillo Comino on 02/03/20 16:32
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 02/03/20 15:38
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.register

import com.onimus.blablasocialmedia.mvvm.common.ProgressViewModel
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import com.onimus.blablasocialmedia.mvvm.exception.*
import com.onimus.blablasocialmedia.mvvm.utils.HandleErrors
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class RegisterViewModel(
    private val repository: UserRepository,
    private val processScheduler: Scheduler = Schedulers.io(),
    private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ProgressViewModel() {

    var email: String? = null
    var password: String? = null
    var registerListener: RegisterListener? = null

    private val handleErrors = HandleErrors()

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    /**
     * button handler
     */
    fun onClickButtonRegister() {
        setActionToAuthenticationButton()
    }

    fun onClickTextViewLogin() {
        //go to fragment_login
        registerListener?.onNavigate()
    }

    /**
     * Action for buttons
     */
    private fun setActionToAuthenticationButton() {
        registerListener?.resetTextInputLayout()

        registerListener?.showProgress()
        //calling repository to perform the actual authentication
        val completable = repository.registerUser(email, password)
        val disposable = getDisposable(completable)
        disposables.add(disposable)
    }

    private fun getDisposable(completable: Completable): Disposable {
        return completable
            .subscribeOn(processScheduler)
            .observeOn(observerScheduler)
            .subscribe({
                registerListener?.hideProgress()
                registerListener?.onSuccessAuth()
            }, {

                registerListener?.hideProgress()

                val error = handleErrors.getMessageError(it)

                when (it) {
                    //Show message in text input
                    is EmailException -> registerListener?.inEmailValidationError(error)
                    //Show message in text input
                    is PasswordException -> registerListener?.inPasswordValidationError(error)
                    //show message
                    else -> registerListener?.onFailureAuth(error)

                }
            })
    }

    override fun onCleared() {
        //onCleared is called when the app is put into the background and the app process is killed in order to free up the system's memory.
        super.onCleared()
        // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
        disposables.dispose()
    }
}
