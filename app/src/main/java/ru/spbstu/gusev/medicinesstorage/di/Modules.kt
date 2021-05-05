package ru.spbstu.gusev.medicinesstorage.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MEDICINES_DATABASE_NAME
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MedicinesDatabase
import ru.spbstu.gusev.medicinesstorage.ui.account.AccountViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.MedicinesViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails.MedicineDetailsViewModel
import ru.spbstu.gusev.medicinesstorage.ui.reminders.RemindersViewModel

fun mainModule() = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            MedicinesDatabase::class.java,
            MEDICINES_DATABASE_NAME
        ).build()
    }
}

fun viewModelsModel() = module {
    viewModel { MedicinesViewModel() }
    viewModel { RemindersViewModel() }
    viewModel { AccountViewModel() }
    viewModel { MedicineDetailsViewModel() }
}