package ru.spbstu.gusev.medicinesstorage.di

import androidx.room.Room
import androidx.work.WorkManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.spbstu.gusev.medicinesstorage.data.local.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MEDICINES_DATABASE_NAME
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MedicinesDatabase
import ru.spbstu.gusev.medicinesstorage.data.local.remiders.RemindersRepository
import ru.spbstu.gusev.medicinesstorage.data.network.medicinesdatabase.*
import ru.spbstu.gusev.medicinesstorage.ui.account.AccountViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.MedicinesViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.scanner.ScannerViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails.MedicineDetailsViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinessearch.MedicinesSearchViewModel
import ru.spbstu.gusev.medicinesstorage.ui.reminders.RemindersViewModel
import ru.spbstu.gusev.medicinesstorage.ui.reminders.addingnewsearch.AddingNewSearchViewModel
import ru.spbstu.gusev.medicinesstorage.ui.reminders.reminderdetails.ReminderDetailsViewModel

fun mainModule() = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            MedicinesDatabase::class.java,
            MEDICINES_DATABASE_NAME
        ).build()
    }
    single { WorkManager.getInstance(androidApplication()) }
    single { MedicinesRepository(get()) }
    single { RemindersRepository(get(), get(), androidApplication()) }
}

fun viewModelsModel() = module {
    viewModel { MedicinesViewModel(get()) }
    viewModel { RemindersViewModel(get(), get()) }
    viewModel { AccountViewModel() }
    viewModel { MedicineDetailsViewModel(get()) }
    viewModel { MedicinesSearchViewModel(get()) }
    viewModel { ScannerViewModel() }
    viewModel { AddingNewSearchViewModel(get()) }
    viewModel { ReminderDetailsViewModel(get(), get()) }
}

fun networkModule() = module {
    factory { provideHttpInterceptor() }
    factory { provideMedicinesDatabaseHttpClient(get()) }
    factory { provideMedicinesDatabaseRetrofit(get()) }
    single { provideMedicinesDatabaseApi(get()) }
    single { MedicinesNetworkRepository(get()) }
}