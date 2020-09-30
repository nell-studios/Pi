package nellStudios.tech.pi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PiApplication: Application() {

    override fun onCreate() = super.onCreate()
}