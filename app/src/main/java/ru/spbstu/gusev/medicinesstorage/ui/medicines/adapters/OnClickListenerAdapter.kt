package ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters

import ru.spbstu.gusev.medicinesstorage.models.Medicine

interface OnClickListenerAdapter {

    fun setOnItemClickListener(action: ItemClickInterface)
}

interface ItemClickInterface {
    fun onItemClickListener(medicine: Medicine)
}