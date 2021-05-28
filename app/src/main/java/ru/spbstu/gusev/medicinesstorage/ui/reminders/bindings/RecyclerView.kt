package ru.spbstu.gusev.medicinesstorage.ui.reminders.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter(value = ["setAdapterReminders"])
fun RecyclerView.setAdapterAndOnItemClickListenerReminder(
    adapter: RecyclerView.Adapter<*>
) {
    this.apply {
        this.adapter = adapter
    }
}

@BindingAdapter(value = ["setAdapterReminders"])
fun RecyclerView.setAdapterAndOnItemClickListenerTriggeredReminder(
    adapter: RecyclerView.Adapter<*>
) {
    this.apply {
        this.adapter = adapter
    }
}