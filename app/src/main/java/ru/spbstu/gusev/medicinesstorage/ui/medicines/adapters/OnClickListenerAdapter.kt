package ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters

import ru.spbstu.gusev.medicinesstorage.ui.medicines.bindings.ItemClickInterface

interface OnClickListenerAdapter {

    fun setOnItemClickListener(action: ItemClickInterface)
}