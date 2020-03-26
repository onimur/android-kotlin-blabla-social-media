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

import com.onimus.blablasocialmedia.mvvm.common.AuthenticationViewModel
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import com.onimus.blablasocialmedia.mvvm.utils.HandleErrors
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class RegisterViewModel(
    private val repository: UserRepository,
    private val processScheduler: Scheduler = Schedulers.io(),
    private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()
) : AuthenticationViewModel() {

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
        registerListener?.resetTextInputLayout()

        val completable = repository.registerUser(email, password).subscribeOn(processScheduler)
            .observeOn(observerScheduler)
        val disposable = disposableToAuthentication(completable, registerListener!!, handleErrors)
        disposables.add(disposable)
    }

    fun onClickTextViewLogin() {
        //go to fragment_login
        registerListener?.onNavigate()
    }

    override fun onCleared() {
        //onCleared is called when the app is put into the background and the app process is killed in order to free up the system's memory.
        super.onCleared()
        // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
        disposables.dispose()
    }
}
