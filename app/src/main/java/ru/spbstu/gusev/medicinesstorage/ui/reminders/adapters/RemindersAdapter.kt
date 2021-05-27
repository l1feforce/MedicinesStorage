package ru.spbstu.gusev.medicinesstorage.ui.reminders.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.gusev.medicinesstorage.databinding.ItemReminderBinding
import ru.spbstu.gusev.medicinesstorage.models.Reminder

class RemindersAdapter : ListAdapter<Reminder, RemindersAdapter.MedicineViewHolder>(Companion) {

    private var onItemClickListener: ((Reminder) -> Unit)? = null
    private var onItemCheckedListener: ((Reminder, View) -> Unit)? = null

    class MedicineViewHolder(val binding: ItemReminderBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemReminderBinding.inflate(layoutInflater, parent, false)

        return MedicineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.reminderItem = currentItem
        if (onItemClickListener != null) {
            holder.binding.root.setOnClickListener {
                onItemClickListener!!.invoke(currentItem)
            }
        }
        onItemCheckedListener?.let {
            holder.binding.onChecked = it
        }
        holder.binding.executePendingBindings()
    }

    fun setOnItemCheckedListener(action: (Reminder, View) -> Unit) {
        onItemCheckedListener = action
    }

    fun setOnItemClickListener(action: (Reminder) -> Unit) {
        onItemClickListener = action
    }
}