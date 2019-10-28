/*
 *
 *  * Created by Murillo Comino on 28/10/19 17:20
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 28/10/19 17:20
 *
 */

package com.onimus.blablasocialmedia.mvvm.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.onimus.blablasocialmedia.R

class ProgressDialog(context: Context) : Dialog(context) {

    override fun create() {
        setCancelable(false)
        val view = layoutInflater.inflate(R.layout.progressbar_dialog, null)
        setContentView(view)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}