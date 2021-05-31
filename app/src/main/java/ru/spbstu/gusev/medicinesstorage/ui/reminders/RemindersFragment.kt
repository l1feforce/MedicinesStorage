package ru.spbstu.gusev.medicinesstorage.ui.reminders

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.checkbox.MaterialCheckBox
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentRemindersBinding
import ru.spbstu.gusev.medicinesstorage.extensions.getColorFromTheme
import ru.spbstu.gusev.medicinesstorage.extensions.hideKeyboard
import ru.spbstu.gusev.medicinesstorage.extensions.setIconsColor
import ru.spbstu.gusev.medicinesstorage.extensions.setupSearch
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.ui.reminders.adapters.HaveToTakeRemindersAdapter
import ru.spbstu.gusev.medicinesstorage.ui.reminders.adapters.RemindersAdapter
import ru.spbstu.gusev.medicinesstorage.ui.reminders.reminderdetails.ReminderDetailsFragment
import ru.spbstu.gusev.medicinesstorage.utils.livedata.EventObserver

class RemindersFragment : Fragment() {

    val viewModel: RemindersViewModel by viewModel()
    private lateinit var binding: FragmentRemindersBinding
    private lateinit var haveToTakeRemindersAdapter: HaveToTakeRemindersAdapter
    private lateinit var remindersAdapter: RemindersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reminders, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel

        haveToTakeRemindersAdapter = HaveToTakeRemindersAdapter().apply {
            setOnItemCheckedListener { reminder, view ->
                val isChecked = (view as MaterialCheckBox).isChecked
                if (isChecked) viewModel.completeNotification(reminder)
            }
            setOnItemSkipListener { viewModel.skipNotification(it)}
        }
        remindersAdapter = RemindersAdapter().apply {
            setOnItemCheckedListener { reminder, view ->
                val isChecked = (view as MaterialCheckBox).isChecked
                if (isChecked) viewModel.addNotifications(reminder)
                else viewModel.removeNotifications(reminder)
            }
            setOnItemClickListener(::openReminder)
        }

        binding.haveToTakeRemindersAdapter = haveToTakeRemindersAdapter
        binding.remindersAdapter = remindersAdapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.haveToTakeRemindersList.observe(viewLifecycleOwner, {
            haveToTakeRemindersAdapter.submitList(it)
        })
        viewModel.filteredRemindersList.observe(viewLifecycleOwner, {
            remindersAdapter.submitList(it)
        })
        viewModel.remindersList.observe(viewLifecycleOwner, {
            viewModel.filteredRemindersList.value = it
        })
        viewModel.addNewReminderEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.navigation_adding_new_search)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_search_menu, menu)
        val menuItem = menu.findItem(R.id.menu_item_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                activity.hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filter(newText)
                return true
            }
        })
        val color = getColorFromTheme(R.attr.colorOnBackground)
        menu.setIconsColor(color)
        menu.setupSearch(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun openReminder(reminder: Reminder) {
        val bundle = bundleOf(
            ReminderDetailsFragment.REMINDER_DETAILS_KEY to reminder
        )
        findNavController().navigate(R.id.navigation_reminder_details, bundle)
    }
}