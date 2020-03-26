/*
 *
 *  * Created by Murillo Comino on 26/03/20 11:26
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 23/03/20 23:07
 *
 */

package com.onimus.blablasocialmedia.mvvm.common

import com.onimus.blablasocialmedia.mvvm.exception.EmailException
import com.onimus.blablasocialmedia.mvvm.exception.PasswordException
import com.onimus.blablasocialmedia.mvvm.utils.HandleErrors
import io.reactivex.Completable
import io.reactivex.disposables.Disposable

abstract class CommonViewModel : ProgressViewModel() {

    protected fun <T : CommonListener> actionToAuthentication(
        completable: Completable,
        listener: T,
        handleErrors: HandleErrors
    ): Disposable {
        listener.showProgress()
        //calling repository to perform the actual authentication
        return completable
            .subscribe({
                listener.hideProgress()
                listener.onSuccessAuth()
            }, {

                listener.hideProgress()

                val error = handleErrors.getMessageError(it)

                when (it) {
                    //Show message in text input
                    is EmailException -> listener.inEmailValidationError(error)
                    //Show message in text input
                    is PasswordException -> listener.inPasswordValidationError(error)
                    //show message
                    else -> listener.onFailureAuth(error)
                }
            })
    }

}