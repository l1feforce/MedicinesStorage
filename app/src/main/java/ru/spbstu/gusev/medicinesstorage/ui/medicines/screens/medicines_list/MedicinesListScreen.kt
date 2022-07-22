package ru.spbstu.gusev.medicinesstorage.ui.medicines.screens.medicines_list

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.get
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.models.Medicine
import ru.spbstu.gusev.medicinesstorage.ui.medicines.MedicinesViewModel
import ru.spbstu.gusev.medicinesstorage.ui.theme.CommonScreenPadding

@Composable
fun MedicinesListScreen(
    viewModel: MedicinesViewModel = get()
) {
    val freshMedicinesList: List<Medicine> by viewModel.freshMedicinesList.observeAsState(emptyList())
    val expiredMedicinesList: List<Medicine> by viewModel.expiredMedicinesList.observeAsState(emptyList())
    LaunchedEffect(key1 = freshMedicinesList) {
        Log.d("test", "MedicinesListScreen: $freshMedicinesList")
    }
    Scaffold(modifier = Modifier.padding(CommonScreenPadding)) {
        LazyColumn(modifier = Modifier.padding(it)) {
            MedicinesListBlock(
                title = R.string.medicines_medicines_title,
                medicineItems = freshMedicinesList
            )
            MedicinesListBlock(
                title = R.string.medicines_expired_title,
                medicineItems = expiredMedicinesList
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.MedicinesListBlock(
    @StringRes title: Int,
    medicineItems: List<Medicine>
) {
    stickyHeader {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.caption,
        )
    }
    items(medicineItems) {
        MedicineItem(medicine = it)
    }
}

@Composable
fun MedicineItem(medicine: Medicine) {
    Text(text = medicine.name)
}