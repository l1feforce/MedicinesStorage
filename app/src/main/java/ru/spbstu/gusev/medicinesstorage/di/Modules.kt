package ru.spbstu.gusev.medicinesstorage.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MEDICINES_DATABASE_NAME
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MedicinesDatabase
import ru.spbstu.gusev.medicinesstorage.data.mock.MedicinesDatabaseMockRepository
import ru.spbstu.gusev.medicinesstorage.data.mock.UserMedicinesMockRepository
import ru.spbstu.gusev.medicinesstorage.data.network.medicinesdatabase.*
import ru.spbstu.gusev.medicinesstorage.ui.account.AccountViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.MedicinesViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.camera.ScannerViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails.MedicineDetailsViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinessearch.MedicinesSearchViewModel
import ru.spbstu.gusev.medicinesstorage.ui.reminders.RemindersViewModel

fun mainModule() = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            MedicinesDatabase::class.java,
            MEDICINES_DATABASE_NAME
        ).build()
    }
    single { UserMedicinesMockRepository() }
    single { MedicinesDatabaseMockRepository() }
}

fun viewModelsModel() = module {
    viewModel { MedicinesViewModel(get()) }
    viewModel { RemindersViewModel() }
    viewModel { AccountViewModel() }
    viewModel { MedicineDetailsViewModel(get()) }
    viewModel { MedicinesSearchViewModel(get()) }
    viewModel { ScannerViewModel() }
}

fun networkModule() = module {
    factory { provideHttpInterceptor() }
    factory { provideMedicinesDatabaseHttpClient(get()) }
    factory { provideMedicinesDatabaseRetrofit(get()) }
    single { provideMedicinesDatabaseApi(get()) }
    single { MedicinesNetworkRepository(get()) }
}