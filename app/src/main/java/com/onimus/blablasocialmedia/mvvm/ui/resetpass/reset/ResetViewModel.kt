/*
 *
 *  * Created by Murillo Comino on 02/03/20 16:42
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 02/03/20 15:38
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.resetpass.reset

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


class ResetViewModel(
    private val repository: UserRepository,
    private val processScheduler: Scheduler = Schedulers.io(),
    private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var resetListener: ResetListener? = null

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
    fun onClickButtonReset() {
        setActionToResetButton()
    }

    /**
     * Action for buttons
     */
    private fun setActionToResetButton() {
        resetListener?.resetTextInputLayout()
        //validate email and password

        //email is valid
        when (val checkEmail = handleErrors.checkEmail(email)) {
            AppConstants.VALID -> {
                resetListener?.resetTextInputLayout()
                //if is valid then show progress
                resetListener?.showProgress()
                //calling repository to perform the actual authentication
                val disposable = getDisposable(repository.onResetPasswordClicked(email!!))
                disposables.add(disposable)
            }
            else -> resetListener?.inEmailValidationError(checkEmail)
        }
    }

    private fun getDisposable(completable: Completable): Disposable {
        return completable
            .subscribeOn(processScheduler)
            .observeOn(observerScheduler)
            .subscribe({
                resetListener?.hideProgress()
                resetListener?.onSuccessAuth()
            }, {

                resetListener?.hideProgress()
                val error = handleErrors.getMessageError(it)
                //show message
                resetListener?.onFailureAuth(error)
            })
    }

    override fun onCleared() {
        //onCleared is called when the app is put into the background and the app process is killed in order to free up the system's memory.
        super.onCleared()
        // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
        disposables.dispose()
    }
}
