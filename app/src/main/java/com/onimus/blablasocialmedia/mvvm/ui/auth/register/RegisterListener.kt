/*
 *
 *  * Created by Murillo Comino on 02/03/20 16:29
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 18/02/20 17:49
 *
 */

package com.onimus.blablasocialmedia.mvvm.ui.auth.register

import com.onimus.blablasocialmedia.mvvm.common.AuthenticationListener

interface RegisterListener: AuthenticationListener {
    fun onNavigate() {}
}