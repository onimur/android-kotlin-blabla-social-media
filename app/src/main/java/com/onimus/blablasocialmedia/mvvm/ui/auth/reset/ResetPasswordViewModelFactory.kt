package com.onimus.blablasocialmedia.mvvm.ui.auth.reset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onimus.blablasocialmedia.mvvm.data.repository.UserRepository
import com.onimus.blablasocialmedia.mvvm.ui.auth.main.MainViewModel


@Suppress("UNCHECKED_CAST")
class ResetPasswordViewModelFactory :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ResetPasswordViewModel() as T
    }
}
