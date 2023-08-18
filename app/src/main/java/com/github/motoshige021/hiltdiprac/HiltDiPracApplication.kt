package com.github.motoshige021.hiltdiprac

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltDiPracApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}