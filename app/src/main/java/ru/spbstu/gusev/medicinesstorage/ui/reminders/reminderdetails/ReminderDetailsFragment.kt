package ru.spbstu.gusev.medicinesstorage.ui.reminders.reminderdetails

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentReminderDetailsBinding
import ru.spbstu.gusev.medicinesstorage.extensions.showSnackbar
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.utils.livedata.EventObserver


class ReminderDetailsFragment : Fragment() {
    companion object {
        const val REMINDER_DETAILS_KEY = "reminder_details"
    }

    val viewModel: ReminderDetailsViewModel by viewModel()
    private lateinit var binding: FragmentReminderDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reminder_details, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel
        binding.onChooseTime = ::onChooseTime

        val reminderDetails = arguments?.getParcelable(REMINDER_DETAILS_KEY) as Reminder?
        reminderDetails?.let {
            viewModel.reminderDetails.value = it.toObservable()
        }

        binding.executePendingBindings()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onSaveEvent.observe(viewLifecycleOwner, EventObserver {
            if (isFieldsValid()) {
                viewModel.performSave()
                val navOptions =
                    NavOptions.Builder().setPopUpTo(R.id.navigation_reminders, true).build()
                findNavController().navigate(R.id.navigation_reminders, null, navOptions)
            }
        })
        viewModel.onCancelEvent.observe(viewLifecycleOwner, EventObserver {
            val navOptions =
                NavOptions.Builder().setPopUpTo(R.id.navigation_reminders, true).build()
            findNavController().navigate(R.id.navigation_reminders, null, navOptions)
        })
        viewModel.reminderDetails.value?.intakes?.observe(viewLifecycleOwner, {
            Log.d("test", "onActivityCreated: newIntakes: $it")
        })
        viewModel.onDoseHelpClickedEvent.observe(viewLifecycleOwner, EventObserver {
            showSnackbar(
                binding.reminderDetailsDoseInput,
                R.string.reminder_details_dosage_help_message
            )
        })
    }

    private fun isFieldsValid(): Boolean {
        var result = true
        if (binding.reminderDetailsDoseInput.editText?.text.toString().isEmpty()) {
            binding.reminderDetailsDoseInput.error = resources.getString(R.string.reminder_details_field_error)
            binding.reminderDetailsDoseInput.editText?.addTextChangedListener { binding.reminderDetailsDoseInput.error = "" }
            result = false
        }
        if (binding.reminderDetailsIntakesAmountInput.editText?.text.toString().isEmpty()) {
            binding.reminderDetailsIntakesAmountInput.error = resources.getString(R.string.reminder_details_field_error)
            binding.reminderDetailsIntakesAmountInput.editText?.addTextChangedListener { binding.reminderDetailsIntakesAmountInput.error = "" }
            result = false
        }
        if (binding.reminderDetailsDurationInput.editText?.text.toString().isEmpty()) {
            binding.reminderDetailsDurationInput.error = resources.getString(R.string.reminder_details_field_error)
            binding.reminderDetailsDurationInput.editText?.addTextChangedListener { binding.reminderDetailsDurationInput.error = "" }
            result = false
        }
        binding.root.clearFocus()
        return result
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.medicine_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_delete -> {
                MaterialAlertDialogBuilder(requireContext()).setMessage(R.string.reminder_details_delete_dialog_message)
                    .setPositiveButton("OK") { dialog, _ ->
                        viewModel.deleteReminder()
                        dialog.dismiss()
                        findNavController().navigateUp()
                    }
                    .setNegativeButton(R.string.medicine_details_cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onChooseTime(): MaterialTimePicker {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
        picker.show(requireActivity().supportFragmentManager, "time_picker")
        return picker
    }

}