package com.onimus.blablasocialmedia.mvvm.ui.resetpass.reset

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
import com.onimus.blablasocialmedia.databinding.FragmentResetPasswordBinding
import com.onimus.blablasocialmedia.mvvm.commons.AuthListener
import com.onimus.blablasocialmedia.mvvm.commons.AuthViewModel
import com.onimus.blablasocialmedia.mvvm.commons.AuthViewModelFactory
import com.onimus.blablasocialmedia.mvvm.extensions.toast
import com.onimus.blablasocialmedia.mvvm.utils.ProgressDialog
import com.onimus.blablasocialmedia.mvvm.utils.sendActionToTextInputLayout
import kotlinx.android.synthetic.main.fragment_login.tiEmail
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ResetPasswordFragment : Fragment(), KodeinAware, AuthListener {

    override val kodein by kodein()

    private val progressBar: ProgressDialog by lazy { ProgressDialog(context!!, R.string.sending) }

    private val factory: AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel
    private lateinit var actionNav: NavDirections

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentResetPasswordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        viewModel.authListener = this
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

    override fun onSuccessAuth() {
        context?.toast("${getString(R.string.email_sent_to)}: ${viewModel.email}")
        actionNav = ResetPasswordFragmentDirections.actionResetPasswordFragmentToSuccessSendingEmailFragment()
        findNavController().navigate(actionNav)
    }

    override fun onFailureAuth(resId: Int) {
        context?.toast("${getString(R.string.error_send_email)}: ${getString(resId)}")
    }

    override fun inEmailValidationError(resId: Int) {
        sendActionToTextInputLayout(getString(resId), tiEmail)
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
    }

}