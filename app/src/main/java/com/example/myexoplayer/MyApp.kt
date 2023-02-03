package com.example.myexoplayer

import android.app.Application
import com.google.android.gms.cast.LaunchOptions
import com.mradzinski.caster.Caster
import java.util.*


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val launchOptions = LaunchOptions.Builder()
            .setLocale(Locale.ENGLISH)
            .setRelaunchIfRunning(false)
            .build()
        Caster.configure(launchOptions)
    }
}