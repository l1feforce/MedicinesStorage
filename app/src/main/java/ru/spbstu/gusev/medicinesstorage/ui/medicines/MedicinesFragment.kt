package ru.spbstu.gusev.medicinesstorage.ui.medicines

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.core.BaseFragment
import ru.spbstu.gusev.medicinesstorage.extensions.getColorFromTheme
import ru.spbstu.gusev.medicinesstorage.extensions.hideKeyboard
import ru.spbstu.gusev.medicinesstorage.extensions.setIconsColor
import ru.spbstu.gusev.medicinesstorage.extensions.setupSearch
import ru.spbstu.gusev.medicinesstorage.models.Medicine
import ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails.MEDICINE_DETAILS_KEY
import ru.spbstu.gusev.medicinesstorage.ui.medicines.screens.medicines_list.MedicinesListScreen
import ru.spbstu.gusev.medicinesstorage.utils.composeContent

class MedicinesFragment : BaseFragment() {

    val viewModel: MedicinesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        return composeContent(requireContext()) {
            MedicinesListScreen(viewModel,
            onAddNewClick = ::onAddNewClick,
            onOpenMedicineClick = ::onOpenMedicineClick)
        }
    }

    private fun onAddNewClick() {
        val navOptions = NavOptions.Builder().apply {
            this.setLaunchSingleTop(true)
        }.build()
        findNavController().navigate(R.id.navigation_medicines_search, null, navOptions)
    }

    private fun onOpenMedicineClick(medicineDetails: Medicine) {
        val bundle = bundleOf(MEDICINE_DETAILS_KEY to medicineDetails)
        findNavController().navigate(R.id.navigation_medicine_details, bundle)
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