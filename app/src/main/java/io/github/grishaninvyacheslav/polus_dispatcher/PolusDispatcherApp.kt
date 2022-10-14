package io.github.grishaninvyacheslav.polus_dispatcher

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import io.github.grishaninvyacheslav.polus_dispatcher.domain.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PolusDispatcherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PolusDispatcherApp)
            modules(appModule)
        }
        MapKitFactory.setApiKey("d15b1b14-c880-4acc-9814-39ea80dcb7e4");
    }
}