package com.example.aichaogumoniqi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StockApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    companion object {
        lateinit var instance: StockApplication
            private set
    }
}