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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.onimus.blablasocialmedia.R
import com.onimus.blablasocialmedia.databinding.FragmentLoginBinding
import com.onimus.blablasocialmedia.mvvm.data.google.GoogleSignInManager
import com.onimus.blablasocialmedia.mvvm.extensions.toast
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

    private val googleSignInManager by lazy {
        GoogleSignInManager(context!!)
    }

    private val factory: LoginViewModelFactory by instance()
    private lateinit var btnSign: SignInButton
    private lateinit var viewModel: LoginViewModel
    private lateinit var actionNav: NavDirections
    private lateinit var task: Task<GoogleSignInAccount>

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
            signInWithGoogleAccount()
        }
    }

    private fun signInWithGoogleAccount() {
        val signInIntent = googleSignInManager.signInIntent
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

    /**
     * Handles authentication of the user's google account in firebase
     */
    override fun onSuccessGoogleSign() {
        val account = task.result
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        //set google email
        viewModel.email = account?.email
        viewModel.firebaseAuthWithGoogle(credential)
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

        if (requestCode == RC_SIGN_IN) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            task = googleSignInManager.getSignedInAccountFromIntent(data)
            viewModel.onClickButtonGoogleSignIn(task)
        }
    }

}
