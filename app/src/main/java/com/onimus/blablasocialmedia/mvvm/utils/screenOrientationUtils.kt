/*
 *
 *  * Created by Murillo Comino on 26/10/19 21:38
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 26/10/19 21:38
 *
 */

package com.onimus.blablasocialmedia.mvvm.utils

import android.app.Activity
import android.content.Context.WINDOW_SERVICE
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.Surface.*
import android.view.WindowManager

fun lockOrientation(activity: Activity) {
    val display =
        (activity.getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay
    val rotation = display.rotation
    val tempOrientation = activity.resources.configuration.orientation
    var orientation = 0
    when (tempOrientation) {
        Configuration.ORIENTATION_LANDSCAPE -> orientation =
            if (rotation == ROTATION_0 || rotation == ROTATION_90)
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else
                ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        Configuration.ORIENTATION_PORTRAIT -> orientation =
            if (rotation == ROTATION_0 || rotation == ROTATION_270)
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            else
                ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
    }
    activity.requestedOrientation = orientation
}

fun unlockOrientation(activity: Activity) {
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}
