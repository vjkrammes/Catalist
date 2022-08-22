package solutions.vjk.catalist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        const val buildTime: Long = BuildConfig.TIMESTAMP
    }
}