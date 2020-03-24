/*
 *
 *  * Created by Murillo Comino on 02/03/20 21:34
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 02/03/20 21:34
 *
 */

package com.onimus.blablasocialmedia.mvvm.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.onimus.blablasocialmedia.mvvm.helper.ProgressViewModelHelper
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class ProgressViewModelTest {

    @get:Rule
    //rule for livedata
    val rule = InstantTaskExecutorRule()

    private var progressViewModelHelper : ProgressViewModelHelper = ProgressViewModelHelper()

    @Test
    fun `the first time getProgressBarStatus is called should return false`() {
        Assert.assertFalse(progressViewModelHelper.getProgressBarStatus().value!!)
    }

    @Test
    fun `getProgressBarStatus should return true`() {
        progressViewModelHelper.setProgressBarStatus(true)
        Assert.assertTrue(progressViewModelHelper.getProgressBarStatus().value!!)
    }
}