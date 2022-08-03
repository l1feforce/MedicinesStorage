package ru.spbstu.gusev.medicinesstorage.ui.medicines.screens.medicines_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.ui.theme.MedicinesEmptyPlaceholderVerticalPadding


@Composable
fun MedicinesEmptyPlaceholder(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.alpha(ContentAlpha.disabled)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_medical_services_24px),
            contentDescription = null
        )
        Spacer(modifier = Modifier.requiredHeight(MedicinesEmptyPlaceholderVerticalPadding))
        Text(
            text = stringResource(id = R.string.medicines_empty_title),
            style = MaterialTheme.typography.h6
        )
    }
}