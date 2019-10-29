/*
 *
 *  * Created by Murillo Comino on 28/10/19 21:32
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 28/10/19 21:32
 *
 */

package com.onimus.blablasocialmedia.mvvm.utils

import com.google.android.material.textfield.TextInputLayout

fun sendActionToTextInputLayout(message: String, textInputLayout: TextInputLayout) {
    textInputLayout.let {
        it.error = message
        it.requestFocus()
        it.isFocusable = true
    }
}