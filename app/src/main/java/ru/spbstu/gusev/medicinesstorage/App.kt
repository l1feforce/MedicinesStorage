@file:Suppress("unused")

package ru.spbstu.gusev.medicinesstorage

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.spbstu.gusev.medicinesstorage.di.mainModule
import ru.spbstu.gusev.medicinesstorage.di.networkModule
import ru.spbstu.gusev.medicinesstorage.di.viewModelsModel

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(networkModule(), mainModule(), viewModelsModel())
        }
    }
}