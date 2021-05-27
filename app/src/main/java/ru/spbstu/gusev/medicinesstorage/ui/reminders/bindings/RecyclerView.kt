package ru.spbstu.gusev.medicinesstorage.ui.reminders.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder
import ru.spbstu.gusev.medicinesstorage.ui.reminders.adapters.HaveToTakeRemindersAdapter
import ru.spbstu.gusev.medicinesstorage.ui.reminders.adapters.ItemClickInterface
import ru.spbstu.gusev.medicinesstorage.ui.reminders.adapters.OnClickListenerAdapter
import ru.spbstu.gusev.medicinesstorage.ui.reminders.adapters.RemindersAdapter

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