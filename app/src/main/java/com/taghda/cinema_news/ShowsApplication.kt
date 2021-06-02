package com.taghda.cinema_news

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShowsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}