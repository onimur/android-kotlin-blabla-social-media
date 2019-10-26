/*
 *
 *  * Created by Murillo Comino on 26/10/19 13:48
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 13:48
 *
 */

package com.onimus.blablasocialmedia.mvvm

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class KodeinApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@KodeinApp))
    }
}