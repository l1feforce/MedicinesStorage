package ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinessearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentMedicinesSearchBinding
import ru.spbstu.gusev.medicinesstorage.extensions.hideKeyboard
import ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters.MedicinesSearchAdapter
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails.MEDICINE_DETAILS_KEY
import ru.spbstu.gusev.medicinesstorage.utils.PermissionsUtil
import ru.spbstu.gusev.medicinesstorage.utils.livedata.EventObserver

class MedicinesSearchFragment : Fragment() {
    companion object {
        const val BARCODE_KEY = "barcode_key"
    }

    val viewModel: MedicinesSearchViewModel by viewModel()
    private lateinit var binding: FragmentMedicinesSearchBinding
    private lateinit var medicinesSearchAdapter: MedicinesSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicines_search, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel

        medicinesSearchAdapter = MedicinesSearchAdapter()
        binding.searchAdapter = medicinesSearchAdapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        readBarcode()

        viewModel.medicinesList.observe(viewLifecycleOwner, { medicinesList ->
            medicinesSearchAdapter.submitList(medicinesList)
        })
        viewModel.searchQuery.observe(viewLifecycleOwner, { searchQuery ->
            if (!searchQuery.isNullOrBlank() && searchQuery != viewModel.oldQuery) {
                viewModel.performMedicinesSearch(searchQuery)
                viewModel.oldQuery = searchQuery
            }
        })
        binding.medicinesSearchEditText.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
            }
            true
        }
        viewModel.openMedicineEvent.observe(viewLifecycleOwner, EventObserver { medicineDetails ->
            val bundle = bundleOf(MEDICINE_DETAILS_KEY to medicineDetails)
            findNavController().navigate(R.id.navigation_medicine_details, bundle)
        })
        viewModel.barcode.observe(viewLifecycleOwner, { barcode ->
            if (!barcode.isNullOrBlank()) {
                viewModel.performBarcodeSearch(barcode)
                arguments?.putString(BARCODE_KEY, "")
            }
        })
        viewModel.onBarcodeClickEvent.observe(viewLifecycleOwner, EventObserver {
            PermissionsUtil.requestPermission(
                requireContext(),
                PermissionsUtil.cameraPermission,
                ""
            ) {
                findNavController().navigate(R.id.navigation_scanner)
            }
        })
    }

    private fun readBarcode() {
        val barcode: String? = arguments?.getString(BARCODE_KEY)
        barcode?.let {
            viewModel.barcode.value = barcode
        }
    }

}