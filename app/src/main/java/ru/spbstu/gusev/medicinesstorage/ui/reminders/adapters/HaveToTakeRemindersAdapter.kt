package ru.spbstu.gusev.medicinesstorage.ui.reminders.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.gusev.medicinesstorage.databinding.ItemReminderHaveToTakeBinding
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder

class HaveToTakeRemindersAdapter : ListAdapter<TriggeredReminder, HaveToTakeRemindersAdapter.MedicineViewHolder>(Companion) {

    private var onItemClickListener: ((TriggeredReminder) -> Unit)? = null
    private var onItemCheckedListener: ((TriggeredReminder, View) -> Unit)? = null
    private var onItemSkipListener: ((TriggeredReminder) -> Unit)? = null

    class MedicineViewHolder(val binding: ItemReminderHaveToTakeBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<TriggeredReminder>() {
        override fun areItemsTheSame(oldItem: TriggeredReminder, newItem: TriggeredReminder): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TriggeredReminder, newItem: TriggeredReminder): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemReminderHaveToTakeBinding.inflate(layoutInflater, parent, false)

        return MedicineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.reminderItem = currentItem
        onItemClickListener?.let { action->
            holder.binding.root.setOnClickListener {
                action.invoke(currentItem)
            }
        }
        onItemCheckedListener?.let {
            holder.binding.onChecked = it
        }
        onItemSkipListener?.let {
            holder.binding.onSkipped = it
        }
        holder.binding.executePendingBindings()
    }

    fun setOnItemCheckedListener(action: (TriggeredReminder, View) -> Unit) {
        onItemCheckedListener = action
    }

    fun setOnItemSkipListener(action: (TriggeredReminder) -> Unit) {
        onItemSkipListener = action
        onItemClickListener = action
    }

    fun setOnItemClickListener(action: (TriggeredReminder) -> Unit) {
        onItemClickListener = action
    }
}