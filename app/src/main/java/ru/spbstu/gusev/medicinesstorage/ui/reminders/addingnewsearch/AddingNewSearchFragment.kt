package ru.spbstu.gusev.medicinesstorage.ui.reminders.addingnewsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentAddingNewSearchBinding
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters.MedicinesAdapter
import ru.spbstu.gusev.medicinesstorage.ui.reminders.reminderdetails.ReminderDetailsFragment
import ru.spbstu.gusev.medicinesstorage.utils.livedata.EventObserver

class AddingNewSearchFragment : Fragment() {

    val viewModel: AddingNewSearchViewModel by viewModel()
    private lateinit var binding: FragmentAddingNewSearchBinding
    private lateinit var medicinesAdapter: MedicinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_adding_new_search, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel

        medicinesAdapter = MedicinesAdapter()
        binding.medicinesAdapter = medicinesAdapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.filteredMedicinesList.observe(viewLifecycleOwner, {
            medicinesAdapter.submitList(it)
        })
        viewModel.openMedicineEvent.observe(viewLifecycleOwner, EventObserver { medicineDetails ->
            val bundle = bundleOf(
                ReminderDetailsFragment.REMINDER_DETAILS_KEY to Reminder(
                    medicineId = medicineDetails.uid,
                    tradeName = medicineDetails.name,
                    dosageForm = medicineDetails.unitsOfMeasure,
                    intakesAmount = 3,
                    intakes = listOf()
                )
            )
            findNavController().navigate(R.id.navigation_reminder_details, bundle)
        })
        viewModel.searchQuery.observe(viewLifecycleOwner, { searchQuery ->
            viewModel.filter(searchQuery)
        })
        viewModel.medicinesList.observe(viewLifecycleOwner, {
            viewModel.filteredMedicinesList.value = it
        })
    }

}