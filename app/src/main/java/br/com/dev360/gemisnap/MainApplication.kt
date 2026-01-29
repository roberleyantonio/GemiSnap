package br.com.dev360.gemisnap

import android.app.Application
import android.os.StrictMode
import br.com.dev360.gemisnap.core.networking.di.NetworkingModules
import br.com.dev360.gemisnap.core.networking.di.RetrofitModule
import br.com.dev360.gemisnap.core.shared.coroutines.CoroutinesModule
import br.com.dev360.gemisnap.core.shared.di.CoreSharedModule
import br.com.dev360.gemisnap.feature.snap.hub.di.snapModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application(){

    private fun defaultModules() = listOf(
        snapModule
    )

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
        }

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@MainApplication)

            modules(
                defaultModules()
                    .asSequence()
                    .plus(CoreSharedModule)
                    .plus(RetrofitModule)
                    .plus(NetworkingModules)
                    .plus(CoroutinesModule)
                    .toList()
            )

        }
    }
}