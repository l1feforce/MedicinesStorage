package ru.spbstu.gusev.medicinesstorage.ui.medicines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentMedicinesBinding
import ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters.MedicinesAdapter
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails.MEDICINE_DETAILS_KEY
import ru.spbstu.gusev.medicinesstorage.utils.EventObserver

class MedicinesFragment : Fragment() {

    val viewModel: MedicinesViewModel by viewModel()
    private lateinit var binding: FragmentMedicinesBinding
    private lateinit var freshMedicinesAdapter: MedicinesAdapter
    private lateinit var expiredMedicinesAdapter: MedicinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_medicines, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel

        freshMedicinesAdapter = MedicinesAdapter()
        expiredMedicinesAdapter = MedicinesAdapter()

        binding.expiredMedicinesAdapter = expiredMedicinesAdapter
        binding.freshMedicinesAdapter = freshMedicinesAdapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.freshMedicinesList.observe(viewLifecycleOwner, Observer {
            freshMedicinesAdapter.submitList(it)
        })
        viewModel.expiredMedicinesList.observe(viewLifecycleOwner, Observer {
            expiredMedicinesAdapter.submitList(it)
        })
        viewModel.openMedicineEvent.observe(viewLifecycleOwner, EventObserver { medicineDetails ->
            val bundle = bundleOf(MEDICINE_DETAILS_KEY to medicineDetails)
            findNavController().navigate(R.id.navigation_medicine_details, bundle)
        })
    }
}