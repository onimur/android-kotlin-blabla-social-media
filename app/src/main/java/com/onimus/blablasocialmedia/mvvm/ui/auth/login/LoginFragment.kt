/*
 *
 *  * Created by Murillo Comino on 26/10/19 18:27
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 18:26
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.onimus.blablasocialmedia.R
import com.onimus.blablasocialmedia.databinding.FragmentLoginBinding
import com.onimus.blablasocialmedia.mvvm.extensions.toast
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants
import com.onimus.blablasocialmedia.mvvm.utils.AppConstants.RC_SIGN_IN
import com.onimus.blablasocialmedia.mvvm.utils.ProgressDialog
import com.onimus.blablasocialmedia.mvvm.utils.sendActionToTextInputLayout
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LoginFragment : Fragment(), KodeinAware, LoginListener {

    override val kodein by kodein()
    private val progressBar: ProgressDialog by lazy {
        ProgressDialog(
            context!!,
            R.string.validating
        )
    }

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(context!!, gso)
    }

    // Configure Google Sign In
    private lateinit var gso: GoogleSignInOptions

    private val factory: LoginViewModelFactory by instance()
    private lateinit var btnSign: SignInButton
    private lateinit var viewModel: LoginViewModel
    private lateinit var actionNav: NavDirections

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        viewModel.loginListener = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        //
        btnSign = binding.root.findViewById(R.id.btnGoogleSignIn)
        initVariables()
        startAction()
        //
        return binding.root
    }

    private fun initVariables() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()



        progressBar.create()

    }

    private fun startAction() {
        //If the view is destroyed while the dialog is visible, its state is saved and retrieved here.
        if (viewModel.getProgressBarStatus().value!! && !progressBar.isShowing) {
            progressBar.show()
        }

        /**
         * Important: Note that you must explicitly call setOnClickListener(OnClickListener).
         * Do not register a listener via XML, or you won't receive your callbacks.
         */
        btnSign.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun inEmailValidationError(resId: Int) {
        sendActionToTextInputLayout(getString(resId), tiEmail)
    }

    override fun inPasswordValidationError(resId: Int) {
        sendActionToTextInputLayout(getString(resId), tiPass)
    }

    override fun onSuccessAuth() {
        context?.toast("${getString(R.string.login_email)}: ${viewModel.email}")
        actionNav = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
        findNavController().navigate(actionNav)
    }

    override fun onFailureAuth(resId: Int) {
        context?.toast("${getString(R.string.authentication_failed)}: ${getString(resId)}")
    }

    override fun onClickTextViewRegister() {
        actionNav = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(actionNav)
    }

    override fun onClickTextViewForgotPassword() {
        actionNav = LoginFragmentDirections.actionLoginFragmentToResetFragment()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                viewModel.onAuthGoogleSignIn(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(AppConstants.Tag.LOG_W, "Google sign in failed", e)
                //
            }
        }
    }

}
