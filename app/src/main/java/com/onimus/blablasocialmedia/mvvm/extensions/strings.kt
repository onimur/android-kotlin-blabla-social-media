/*
 *
 *  * Created by Murillo Comino on 26/10/19 19:34
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 19:34
 *
 */

package com.onimus.blablasocialmedia.mvvm.extensions

import java.util.regex.Pattern

fun String.patternMatch(pattern: Pattern): Boolean =
    pattern.matcher(this).matches()