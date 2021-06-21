package ru.spbstu.gusev.medicinesstorage.ui.reminders.bindings

import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.models.Time
import ru.spbstu.gusev.medicinesstorage.models.toTime

@BindingAdapter("intakesAmount", "onChooseTimeIntakes")
fun LinearLayout.setIntakesAmount(
    amount: Int,
    onChooseTimeAction: () -> MaterialTimePicker
) {
    if (amount < 0) return
    val intakes = getIntakes()
    if (amount == intakes.size) return
    if (amount > intakes.size) {
        for (i in intakes.size + 1..amount) {
            this.addIntakeItem(i, onChooseTimeAction = onChooseTimeAction)
        }
    }
    if (amount < intakes.size) {
        this.setIntakes(intakes.subList(0, amount), onChooseTimeAction)
    }
}

@BindingAdapter("intakes", "onChooseTime")
fun LinearLayout.setIntakes(
    intakes: List<Time>,
    onChooseTimeAction: () -> MaterialTimePicker
) {
    val currentIntakes = getIntakes()
    if (currentIntakes != intakes) {
        this.removeAllViews()
        for (i in 1..intakes.size) {
            val time = intakes[i - 1]
            addIntakeItem(i, time, onChooseTimeAction)
        }
    }
}

@InverseBindingAdapter(attribute = "intakes")
fun LinearLayout.getIntakes(): List<Time> {
    val result = mutableListOf<Time>()
    this.forEachIndexed { index, view ->
        val inputLayout = view.findViewById<TextInputLayout>(R.id.item_take_time_input)
        val timeString = (inputLayout.editText as? AutoCompleteTextView)?.text.toString()
        result.add(timeString.toTime())
    }
    return result
}

@BindingAdapter("app:intakesAttrChanged")
fun LinearLayout.setIntakesListener(attrChange: InverseBindingListener) {
    this.addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
        attrChange.onChange()
        this.forEach { view ->
            if (view is ConstraintLayout) {
                val inputLayout = view.findViewById<TextInputLayout>(R.id.item_take_time_input)
                (inputLayout.editText as? AutoCompleteTextView)?.apply {
                    this.addTextChangedListener { attrChange.onChange() }
                }
            }
        }
    }
}

fun LinearLayout.addIntakeItem(
    orderNumber: Int,
    time: Time? = null,
    onChooseTimeAction: () -> MaterialTimePicker
) {
    val newItem =
        LayoutInflater.from(this.context)
            .inflate(R.layout.item_intake_time, null) as ConstraintLayout
    newItem.apply {
        val titleTv = findViewById<TextView>(R.id.item_take_time_title_tv)
        val inputLayout = findViewById<TextInputLayout>(R.id.item_take_time_input)

        titleTv.text = resources.getString(R.string.item_take_time_title, orderNumber)
        val items = resources.getStringArray(R.array.item_take_time_spinner)
        val adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, items)
        (inputLayout.editText as? AutoCompleteTextView)?.apply {
            time?.let { setText(time.toString()) } ?: setText(items[(orderNumber - 1) % 3])
            setAdapter(adapter)
            setOnItemClickListener { parent, view, position, id ->
                if (position == items.size - 1) {
                    val materialTimePicker = onChooseTimeAction.invoke()
                    materialTimePicker.addOnPositiveButtonClickListener {
                        setText(
                            Time(materialTimePicker.hour, materialTimePicker.minute).toString(),
                            false
                        )
                    }
                }
            }
        }
    }
    this.addView(newItem)
}