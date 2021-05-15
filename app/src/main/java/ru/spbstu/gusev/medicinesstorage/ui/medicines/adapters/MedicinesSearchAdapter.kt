package ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.databinding.ItemMedicineBinding
import ru.spbstu.gusev.medicinesstorage.databinding.ItemMedicinesSearchBinding
import ru.spbstu.gusev.medicinesstorage.ui.medicines.bindings.ItemClickInterface

class MedicinesSearchAdapter : ListAdapter<Medicine, MedicinesSearchAdapter.MedicineViewHolder>(Companion), OnClickListenerAdapter {

    private lateinit var onItemClickListener: ItemClickInterface

    class MedicineViewHolder(val binding: ItemMedicinesSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<Medicine>() {
        override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMedicinesSearchBinding.inflate(layoutInflater, parent, false)

        return MedicineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.medicineItem = currentItem
        if (::onItemClickListener.isInitialized) {
            holder.binding.root.setOnClickListener {
                onItemClickListener.onItemClickListener(currentItem)
            }
        }
        holder.binding.executePendingBindings()
    }

    override fun setOnItemClickListener(action: ItemClickInterface) {
        onItemClickListener = action
    }
}