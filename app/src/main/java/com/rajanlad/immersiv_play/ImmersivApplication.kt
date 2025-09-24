package com.rajanlad.immersiv_play

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ImmersivApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // Initialization code
    }
}