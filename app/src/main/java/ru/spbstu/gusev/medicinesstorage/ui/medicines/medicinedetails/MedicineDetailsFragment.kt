package ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentMedicineDetailsBinding
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentMedicinesBinding
import ru.spbstu.gusev.medicinesstorage.ui.medicines.MedicinesViewModel
import ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters.MedicinesAdapter

const val MEDICINE_DETAILS_KEY = "medicine_details"

class MedicineDetailsFragment : Fragment() {

    val viewModel: MedicineDetailsViewModel by viewModel()
    private lateinit var binding: FragmentMedicineDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_details, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel

        val medicineDetails = arguments?.getParcelable(MEDICINE_DETAILS_KEY) as Medicine?
        medicineDetails?.let { viewModel.medicineDetails.value = it }

        return binding.root
    }

}