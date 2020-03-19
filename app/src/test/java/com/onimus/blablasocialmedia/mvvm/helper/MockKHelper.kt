/*
 *
 *  * Created by Murillo Comino on 18/03/20 10:02
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 18/03/20 10:02
 *
 */

package com.onimus.blablasocialmedia.mvvm.helper

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import io.mockk.*
import io.mockk.impl.annotations.MockK


class MockKHelper<TResult : Any?> {

    @MockK
    private var task: Task<TResult> = mockk(relaxed = true)

    private val slotComplete = slot<OnCompleteListener<TResult>>()
    private val slotFailure = slot<OnFailureListener>()


    init {
        MockKAnnotations.init(this)
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
    }

    fun initializeMockks(vararg mockTask: () -> Task<TResult>) {
        mockTask.forEach { every { it.invoke() } returns task }
    }

    fun initializeCapture() {
        every { task.addOnCompleteListener(capture(slotComplete)) } returns task
        every { task.addOnFailureListener(capture(slotFailure)) } returns task
    }

    fun taskSuccessful(value: Boolean = true) {
        every { task.isSuccessful } returns value
    }

    /**
     * Only use after the method you want to capture is triggered.
     * @param failure If the variable is empty then you want to capture the complete slot.
     */
    fun slotCaptured(failure: Exception? = null) {
        if (failure == null) slotComplete.captured.onComplete(task)
        else slotFailure.captured.onFailure(failure)
    }
}