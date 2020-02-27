/*
 *
 *  * Created by Murillo Comino on 26/10/19 16:36
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 16:36
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.main

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
import com.onimus.blablasocialmedia.databinding.FragmentMainBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class MainFragment : Fragment(), KodeinAware, MainListener {
    override val kodein by kodein()

    private val factory: MainViewModelFactory by instance()

    private lateinit var viewModel: MainViewModel

    private lateinit var actionNav: NavDirections

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        viewModel.mainListener = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        viewModel.checkUserStatus()
        super.onStart()
    }

    override fun onRegisterClicked() {
        //go to fragment_register
        actionNav = MainFragmentDirections.actionMainFragmentToRegisterFragment()
        findNavController().navigate(actionNav)
    }

    override fun onLoginClicked() {
        //go to fragment_login
        actionNav = MainFragmentDirections.actionMainFragmentToLoginFragment()
        findNavController().navigate(actionNav)
    }

    override fun onUserLogged() {
        //go to fragment_profile
        actionNav = MainFragmentDirections.actionMainFragmentToProfileFragment()
        findNavController().navigate(actionNav)
    }
}
