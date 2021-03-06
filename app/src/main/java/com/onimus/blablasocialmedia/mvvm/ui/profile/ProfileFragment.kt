/*
 *
 *  * Created by Murillo Comino on 26/10/19 14:03
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 14:03
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.profile

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
import com.onimus.blablasocialmedia.databinding.FragmentProfileBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ProfileFragment : Fragment(), KodeinAware, ProfileListener {
    override val kodein by kodein()

    private val factory: ProfileViewModelFactory by instance()
    private lateinit var viewModel: ProfileViewModel

    private lateinit var actionNav: NavDirections

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        viewModel.profileListener = this
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        viewModel.checkUserStatus()
        super.onStart()
    }

    override fun onLogout() {
        //go to mainFragment
        actionNav = ProfileFragmentDirections.actionProfileFragmentToMainFragment()
        findNavController().navigate(actionNav)
    }
}
