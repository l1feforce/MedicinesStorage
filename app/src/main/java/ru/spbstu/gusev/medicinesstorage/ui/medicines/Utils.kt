package ru.spbstu.gusev.medicinesstorage.ui.medicines

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.models.Medicine

fun String.getMedicineIconByDosageForm(): Int {
    return when (this) {
        "табл." -> R.drawable.ic_tablets
        "мл." -> R.drawable.ic_syrop
        "капс." -> R.drawable.ic_pills
        else -> R.drawable.ic_other_medicines
    }
}

fun Medicine.getResidue(): String {
    val medicine = this
    return "${medicine.residue}/${medicine.volume}"
}

@Composable
fun Medicine.getResidueColor(): Color {
    return if (this.residue.toDouble() / this.volume <= 0.3) {
        colorResource(id = R.color.red_error)
    } else {
        MaterialTheme.colors.onBackground
    }
}