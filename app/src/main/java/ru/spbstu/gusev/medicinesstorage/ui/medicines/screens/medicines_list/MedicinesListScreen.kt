package ru.spbstu.gusev.medicinesstorage.ui.medicines.screens.medicines_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.get
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.models.Medicine
import ru.spbstu.gusev.medicinesstorage.ui.animations.CrossfadeVisibility
import ru.spbstu.gusev.medicinesstorage.ui.medicines.MedicinesViewModel
import ru.spbstu.gusev.medicinesstorage.ui.theme.CommonScreenPadding
import ru.spbstu.gusev.medicinesstorage.utils.showIf

@Composable
fun MedicinesListScreen(
    viewModel: MedicinesViewModel = get(),
    onAddNewClick: () -> Unit,
    onOpenMedicineClick: (medicine: Medicine) -> Unit
) {
    Scaffold(
        modifier = Modifier.padding(CommonScreenPadding),
        floatingActionButton = {
            ExtendedFabButton(onClick = onAddNewClick)
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            MedicinesLazyColumn(
                viewModel,
                onOpenMedicineClick,
                Modifier.fillMaxSize()
            )

            CrossfadeVisibility(viewModel.isMedicinesListEmpty() && !viewModel.isLoading) {
                MedicinesEmptyPlaceholder(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun MedicinesLazyColumn(
    viewModel: MedicinesViewModel,
    onClick: (Medicine) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        showIf(!viewModel.isFreshMedicinesListEmpty()) {
            MedicinesListBlock(
                title = R.string.medicines_medicines_title,
                medicineItems = viewModel.freshMedicinesListState,
                onClick = onClick
            )
        }
        showIf(!viewModel.isExpiredMedicinesListEmpty()) {
            MedicinesListBlock(
                title = R.string.medicines_expired_title,
                medicineItems = viewModel.expiredMedicinesListState,
                onClick = onClick
            )
        }
    }
}

@Composable
fun ExtendedFabButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        text = { Text(text = stringResource(id = R.string.medicines_extended_fab_label)) },
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus_24dp),
                contentDescription = stringResource(
                    id = R.string.medicines_extended_fab_label
                )
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        modifier = modifier
    )
}