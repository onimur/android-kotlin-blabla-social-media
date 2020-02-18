package com.onimus.blablasocialmedia.mvvm.ui.auth.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.onimus.blablasocialmedia.R
import com.onimus.blablasocialmedia.databinding.MainFragmentBinding
import com.onimus.blablasocialmedia.mvvm.ui.auth.main.MainViewModel
import com.onimus.blablasocialmedia.mvvm.ui.auth.main.MainViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ResetPasswordFragment : Fragment(), KodeinAware, ResetPasswordListener {

    override val kodein by kodein()

    private val factory: ResetPasswordViewModelFactory by instance()

    private lateinit var viewModel: ResetPasswordViewModel

}