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

package com.onimus.blablasocialmedia.mvvm.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.onimus.blablasocialmedia.R
import com.onimus.blablasocialmedia.databinding.FragmentRegisterBinding
import com.onimus.blablasocialmedia.mvvm.extensions.toast
import com.onimus.blablasocialmedia.mvvm.utils.ProgressDialog
import com.onimus.blablasocialmedia.mvvm.utils.sendActionToTextInputLayout
import kotlinx.android.synthetic.main.fragment_register.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class RegisterFragment : Fragment(), KodeinAware, RegisterListener {

    override val kodein by kodein()

    private val progressBar: ProgressDialog by lazy { ProgressDialog(requireContext(), R.string.validating) }

    private val factory: RegisterViewModelFactory by instance()

    private lateinit var viewModel: RegisterViewModel
    private lateinit var actionNav: NavDirections

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        viewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)
        viewModel.registerListener = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        //
        initVariables()
        startAction()
        //
        return binding.root
    }

    private fun initVariables() {
        progressBar.create()
    }

    private fun startAction() {
        //If the view is destroyed while the dialog is visible, its state is saved and retrieved here.
        if (viewModel.getProgressBarStatus().value!! && !progressBar.isShowing) {
            progressBar.show()
        }
    }

    override fun inEmailValidationError(resId: Int) {
        sendActionToTextInputLayout(getString(resId), tiEmail)
    }

    override fun inPasswordValidationError(resId: Int) {
        sendActionToTextInputLayout(getString(resId), tiPass)
    }

    override fun onSuccessAuth() {
        requireContext().toast("${getString(R.string.registered_email)}: ${viewModel.email}")
        actionNav = RegisterFragmentDirections.actionRegisterFragmentToProfileFragment()
        findNavController().navigate(actionNav)
    }

    override fun onFailureAuth(resId: Int) {
        requireContext().toast("${getString(R.string.authentication_failed)}: ${getString(resId)}")
    }

    override fun onNavigate() {
        actionNav = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(actionNav)
    }

    override fun showProgress() {
        progressBar.show()
        //Save dialog state
        viewModel.setProgressBarStatus(true)
    }

    override fun hideProgress() {
        progressBar.dismiss()
        viewModel.setProgressBarStatus(false)
    }

    override fun resetTextInputLayout() {
        tiEmail.error = null
        tiPass.error = null
    }

    ///////////////////////////////////////////////////////////////////////////////////

}
