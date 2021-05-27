package ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dialog.plus.ui.DialogPlusBuilder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.databinding.DialogMedicineDetailsEditResidueBinding
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentMedicineDetailsBinding
import ru.spbstu.gusev.medicinesstorage.ui.medicines.models.ResidueDetails
import ru.spbstu.gusev.medicinesstorage.utils.livedata.EventObserver
import java.util.*


const val MEDICINE_DETAILS_KEY = "medicine_details"

class MedicineDetailsFragment : Fragment() {

    val viewModel: MedicineDetailsViewModel by viewModel()
    private lateinit var binding: FragmentMedicineDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_details, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel

        val medicineDetails = arguments?.getParcelable(MEDICINE_DETAILS_KEY) as Medicine?
        medicineDetails?.let { viewModel.medicineDetails.value = it }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel.medicineDetails.observe(viewLifecycleOwner, { medicineDetails: Medicine? ->
            viewModel.residueDetails.value = ResidueDetails(
                MutableLiveData(
                    medicineDetails?.unitsOfMeasure ?: ""
                ), medicineDetails?.volume ?: 0, medicineDetails?.residue ?: 0
            )
        })
        viewModel.editUseUntilEvent.observe(viewLifecycleOwner, EventObserver {
            DialogPlusBuilder()
                .setHeaderBgColor(R.color.purple_500)
                .setTextColors(R.color.black, R.color.black, R.color.white)
                .setTexts("OK")
                .buildMonthYearPickerDialog(2100, 2000, false) { year: Int, month: Int ->
                    val calendar = Calendar.getInstance().apply { set(year, month - 1, 1) }
                    viewModel.medicineDetails.value?.let { medicineDetails ->
                        viewModel.medicineDetails.value =
                            medicineDetails.copy(
                                useUntil = calendar.timeInMillis / 1000
                            )
                    }
                }.show(requireActivity().supportFragmentManager, "tag")
        })
        viewModel.editResidueEvent.observe(viewLifecycleOwner, EventObserver {
            val dialogBinding: DialogMedicineDetailsEditResidueBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_medicine_details_edit_residue,
                null,
                false
            )
            dialogBinding.viewmodel = viewModel
            dialogBinding.lifecycleOwner = this

            MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root)
                .setPositiveButton(R.string.dialog_medicine_details_edit_residue_ok) { dialog, _ ->
                    val residueDetails = viewModel.residueDetails.value
                    viewModel.medicineDetails.value?.let { medicineDetails ->
                        viewModel.medicineDetails.value =
                            medicineDetails.copy(
                                volume = residueDetails?.volume ?: 0,
                                unitsOfMeasure = residueDetails?.unitsOfMeasure?.value ?: "",
                                residue = residueDetails?.residue ?: 0
                            )
                    }
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_medicine_details_edit_residue_cancel) { dialog: DialogInterface, _ ->
                    dialog.dismiss()
                }
                .show()
        })
        viewModel.openInstructionEvent.observe(viewLifecycleOwner, EventObserver { url ->
            if (url.isNotBlank()) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        })
        viewModel.onCancelEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigateUp()
        })
        viewModel.onSaveEvent.observe(viewLifecycleOwner, EventObserver {
            val navOptions =
                NavOptions.Builder().setPopUpTo(R.id.navigation_medicines, true).build()
            findNavController().navigate(R.id.navigation_medicines, null, navOptions)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.medicine_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_delete -> {
                MaterialAlertDialogBuilder(requireContext()).setMessage(R.string.medicine_details_delete_dialog_message)
                    .setPositiveButton("OK") { dialog, _ ->
                        viewModel.deleteMedicine()
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

}