package ru.spbstu.gusev.medicinesstorage.ui.medicines.camera

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentMedicinesSearchBinding
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentScannerBinding
import ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters.MedicinesSearchAdapter
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinessearch.MedicinesSearchFragment
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinessearch.MedicinesSearchViewModel
import ru.spbstu.gusev.medicinesstorage.utils.Event
import ru.spbstu.gusev.medicinesstorage.utils.EventObserver

class ScannerFragment : Fragment() {

    val viewModel: ScannerViewModel by viewModel()
    private lateinit var binding: FragmentScannerBinding
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel
        codeScanner = CodeScanner(requireActivity(), binding.scannerView)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        codeScanner.decodeCallback = DecodeCallback {
            viewModel.barcode.postValue(Event(it.text))
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.barcode.observe(viewLifecycleOwner, EventObserver {
            val bundle = bundleOf(MedicinesSearchFragment.BARCODE_KEY to it)
            findNavController().navigate(R.id.navigation_medicines_search, bundle)
        })
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

}