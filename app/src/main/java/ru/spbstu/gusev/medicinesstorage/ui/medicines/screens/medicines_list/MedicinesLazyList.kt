package ru.spbstu.gusev.medicinesstorage.ui.medicines.screens.medicines_list

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.spbstu.gusev.medicinesstorage.models.Medicine
import ru.spbstu.gusev.medicinesstorage.ui.medicines.getMedicineIconByDosageForm
import ru.spbstu.gusev.medicinesstorage.ui.medicines.getResidue
import ru.spbstu.gusev.medicinesstorage.ui.medicines.getResidueColor

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.MedicinesListBlock(
    @StringRes title: Int,
    medicineItems: List<Medicine>,
    onClick: (Medicine) -> Unit
) {
    stickyHeader {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.caption,
        )
    }
    items(medicineItems) {
        MedicineItem(medicine = it, onClick = { onClick(it) })
    }
}

@Composable
fun MedicineItem(medicine: Medicine, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = medicine.unitsOfMeasure.getMedicineIconByDosageForm()),
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
        Column(Modifier.fillMaxWidth(0.75f)) {
            Text(
                text = medicine.name,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
            )
            Text(
                text = medicine.packageDescription,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.alpha(ContentAlpha.medium)
            )
        }
        Text(
            text = medicine.getResidue(),
            style = MaterialTheme.typography.caption,
            color = medicine.getResidueColor(),
            modifier = Modifier
        )
    }
}