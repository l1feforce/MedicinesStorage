package ru.spbstu.gusev.medicinesstorage.ui.medicines

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentMedicinesBinding
import ru.spbstu.gusev.medicinesstorage.extensions.getColorFromTheme
import ru.spbstu.gusev.medicinesstorage.extensions.hideKeyboard
import ru.spbstu.gusev.medicinesstorage.extensions.setIconsColor
import ru.spbstu.gusev.medicinesstorage.extensions.setupSearch
import ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters.MedicinesAdapter
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails.MEDICINE_DETAILS_KEY
import ru.spbstu.gusev.medicinesstorage.utils.livedata.EventObserver

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
        setHasOptionsMenu(true)
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

        viewModel.freshMedicinesList.observe(viewLifecycleOwner, {
            freshMedicinesAdapter.submitList(it)
        })
        viewModel.expiredMedicinesList.observe(viewLifecycleOwner, {
            expiredMedicinesAdapter.submitList(it)
        })
        viewModel.openMedicineEvent.observe(viewLifecycleOwner, EventObserver { medicineDetails ->
            val bundle = bundleOf(MEDICINE_DETAILS_KEY to medicineDetails)
            findNavController().navigate(R.id.navigation_medicine_details, bundle)
        })
        viewModel.addNewMedicineEvent.observe(viewLifecycleOwner, EventObserver {

            /*MaterialAlertDialogBuilder(requireContext())
                .setItems(resources.getStringArray(R.array.medicines_add_type_array)) { dialog, which ->
                    when (which) {
                        1 -> findNavController().navigate(R.id.navigation_medicines_search)
                        else -> {
                            PermissionsUtil.requestPermission(
                                requireContext(),
                                PermissionsUtil.cameraPermission,
                                ""
                            ) {
                                findNavController().navigate(R.id.navigation_scanner)
                            }
                        }
                    }
                }.show()*/
            val navOptions = NavOptions.Builder().apply { /*setPopUpTo(R.id.navigation_medicines, true)*/
                this.setLaunchSingleTop(true)}.build()
            findNavController().navigate(R.id.navigation_medicines_search, null, navOptions)
        })
        viewModel.medicinesList.observe(viewLifecycleOwner, {
            viewModel.filteredMedicinesList.value = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
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
}