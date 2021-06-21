package ru.spbstu.gusev.medicinesstorage.ui.reminders.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import ru.spbstu.gusev.medicinesstorage.R

@BindingAdapter("srcRes")
fun ImageView.setPhoto(id: Int) {
    setImageResource(id)//Glide.with(this).load(id).into(this)
}

/*<item>табл.</item>
<item>шт.</item>
<item>доз.</item>
<item>мл.</item>
<item>капс.</item>*/
@BindingAdapter("photoByDosageForm")
fun ImageView.setPhotoByDosageForm(dosageForm: String) {
    val resourceId = when(dosageForm) {
        "табл." -> R.drawable.ic_tablets
        "мл." -> R.drawable.ic_syrop
        "капс." -> R.drawable.ic_pills
        else -> R.drawable.ic_other_medicines
    }
    setImageResource(resourceId)
}