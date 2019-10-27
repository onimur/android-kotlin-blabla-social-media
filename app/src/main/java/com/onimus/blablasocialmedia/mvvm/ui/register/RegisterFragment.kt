/*
 *
 *  * Created by Murillo Comino on 26/10/19 18:28
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 18:26
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.register


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.onimus.blablasocialmedia.R
import com.onimus.blablasocialmedia.databinding.RegisterFragmentBinding
import com.onimus.blablasocialmedia.mvvm.commons.AuthListener
import com.onimus.blablasocialmedia.mvvm.commons.AuthViewModel
import com.onimus.blablasocialmedia.mvvm.commons.AuthViewModelFactory
import com.onimus.blablasocialmedia.mvvm.extensions.toast
import com.onimus.blablasocialmedia.mvvm.utils.lockOrientation
import com.onimus.blablasocialmedia.mvvm.utils.unlockOrientation
import kotlinx.android.synthetic.main.register_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class RegisterFragment : Fragment(), KodeinAware, AuthListener {

    override val kodein by kodein()

    private val factory: AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel
    private lateinit var actionNav: NavDirections

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: RegisterFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        viewModel.authListener = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun inEmailValidationError(resId: Int) {
        sendActionToTextInputLayout(getString(resId), tiEmail)
    }

    override fun inPasswordValidationError(resId: Int) {
        sendActionToTextInputLayout(getString(resId), tiPass)
    }

    override fun onSuccessAuth() {
        context?.toast("${getString(R.string.registered_email)}: ${viewModel.email}")
        actionNav = RegisterFragmentDirections.actionRegisterFragmentToProfileFragment()
        findNavController().navigate(actionNav)
    }

    override fun onFailureAuth(redId: Int) {
        context?.toast("${getString(R.string.authentication_failed)}: ${getString(redId)}")
    }

    override fun onNavigateToLogin() {
        actionNav = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(actionNav)
    }

    override fun showProgress() {
        llProgress.visibility = View.VISIBLE
        btnRegister.isEnabled = false
        etEmail.isEnabled = false
        etPass.isEnabled = false

        lockOrientation(activity!!)
    }

    override fun hideProgress() {
        llProgress.visibility = View.GONE
        btnRegister.isEnabled = true
        etEmail.isEnabled = true
        etPass.isEnabled = true
        unlockOrientation(activity!!)
    }

    override fun resetTextInputLayout() {
        tiEmail.error = null
        tiPass.error = null
    }

    ///////////////////////////////////////////////////////////////////////////////////
    private fun sendActionToTextInputLayout(message: String, textInputLayout: TextInputLayout) {
        textInputLayout.let {
            it.error = message
            it.requestFocus()
            it.isFocusable = true
        }

    }
}
