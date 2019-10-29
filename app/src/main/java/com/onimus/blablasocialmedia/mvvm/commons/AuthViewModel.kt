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
import com.google.firebase.auth.FirebaseAuthException
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import com.onimus.blablasocialmedia.mvvm.extensions.patternMatch
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_CREDENTIAL_ALREADY_IN_USE
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_CUSTOM_TOKEN_MISMATCH
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_EMAIL_ALREADY_IN_USE
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_INVALID_CREDENTIAL
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_INVALID_CUSTOM_TOKEN
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_INVALID_EMAIL
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_INVALID_USER_TOKEN
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_OPERATION_NOT_ALLOWED
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_REQUIRES_RECENT_LOGIN
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_USER_DISABLED
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_USER_MISMATCH
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_USER_NOT_FOUND
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_USER_TOKEN_EXPIRED
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_WEAK_PASSWORD
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.ErrorFirebaseAuth.ERROR_WRONG_PASSWORD
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import androidx.lifecycle.MutableLiveData
import com.onimus.blablasocialmedia.R


class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null

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
        val check = checkEmailAndPassword()
        if (check) {
            authListener?.resetTextInputLayout()
            //if is valid then show progress
            authListener?.showProgress()
            //calling onRegisterClicked from repository to perform the actual authentication
            val disposable = repository.onRegisterClicked(email!!, password!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    authListener?.hideProgress()
                    authListener?.onSuccessAuth()
                }, {

                    authListener?.hideProgress()
                    val error = getMessageError(it)
                    //show message
                    authListener?.onFailureAuth(error)
                })
            disposables.add(disposable)
        }
    }

    fun onClickButtonLogin() {
        authListener?.resetTextInputLayout()
        //validate email and password
        val check = checkEmailAndPassword()
        if (check) {
            authListener?.resetTextInputLayout()
            //if is valid then show progress
            authListener?.showProgress()
            //calling onLoginClicked from repository to perform the actual authentication
            val disposable = repository.onLoginClicked(email!!, password!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    authListener?.hideProgress()
                    authListener?.onSuccessAuth()
                }, {

                    authListener?.hideProgress()
                    val error = getMessageError(it)
                    //show message
                    authListener?.onFailureAuth(error)
                })
            disposables.add(disposable)
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

    private fun checkEmailAndPassword(): Boolean {
        when {
            email.isNullOrBlank() -> {
                authListener?.inEmailValidationError(R.string.field_empty)
                return false
            }
            !email!!.patternMatch(AppConstants.Pattern.EMAIL) -> {
                authListener?.inEmailValidationError(R.string.error_invalid_email_format)
                return false
            }
            password.isNullOrBlank() -> {
                authListener?.inPasswordValidationError(R.string.field_empty)
                return false
            }
            !password!!.patternMatch(AppConstants.Pattern.PASSWORD) -> {
                authListener?.inPasswordValidationError(R.string.error_password_weak)
                return false
            }
            else -> return true
        }
    }

    private fun getMessageError(it: Throwable): Int {
        if (it is FirebaseAuthException) {
            return when (it.errorCode) {
                ERROR_EMAIL_ALREADY_IN_USE -> R.string.error_email_already_use
                ERROR_INVALID_EMAIL -> R.string.error_invalid_email_format
                ERROR_WEAK_PASSWORD -> R.string.error_password_weak
                ERROR_CREDENTIAL_ALREADY_IN_USE -> R.string.error_credentials_already_use
                ERROR_INVALID_CUSTOM_TOKEN -> R.string.error_invalid_custom_token
                ERROR_CUSTOM_TOKEN_MISMATCH -> R.string.error_custom_token_mismatch
                ERROR_INVALID_CREDENTIAL -> R.string.error_invalid_credential
                ERROR_WRONG_PASSWORD -> R.string.error_wrong_password
                ERROR_USER_MISMATCH -> R.string.error_user_mismatch
                ERROR_REQUIRES_RECENT_LOGIN -> R.string.error_requires_recent_login
                ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL -> R.string.error_account_exists_with_different_credential
                ERROR_USER_DISABLED -> R.string.error_user_disabled
                ERROR_USER_TOKEN_EXPIRED -> R.string.error_user_token_expired
                ERROR_USER_NOT_FOUND -> R.string.error_user_not_found
                ERROR_INVALID_USER_TOKEN -> R.string.error_invalid_user_token
                ERROR_OPERATION_NOT_ALLOWED -> R.string.error_operation_not_allowed
                else -> R.string.error_unknown
            }
        } else {
            return R.string.error_unknown
        }
    }
}
