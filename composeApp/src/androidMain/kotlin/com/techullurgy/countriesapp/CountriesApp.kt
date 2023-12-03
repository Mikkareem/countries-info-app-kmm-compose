package com.techullurgy.countriesapp

import android.app.Application
import di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CountriesApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CountriesApp)
            modules(appModule)
        }
    }
}