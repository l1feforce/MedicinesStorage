package ru.spbstu.gusev.medicinesstorage.ui.medicines

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.models.Medicine

class MedicinesViewModel(private val medicinesRepository: MedicinesRepository) : ViewModel() {

    private val currentTimeSeconds: Long
        get() = System.currentTimeMillis() / 1000

    private var medicinesListState by mutableStateOf(emptyList<Medicine>())
    var filteredMedicinesListState by mutableStateOf(emptyList<Medicine>())
    fun isMedicinesListEmpty() = filteredMedicinesListState.isEmpty()

    var isLoading by mutableStateOf(false)

    var freshMedicinesListState by mutableStateOf(emptyList<Medicine>())
    fun isFreshMedicinesListEmpty() = freshMedicinesListState.isEmpty()

    var expiredMedicinesListState by mutableStateOf(emptyList<Medicine>())
    fun isExpiredMedicinesListEmpty() = expiredMedicinesListState.isEmpty()

    init {
        viewModelScope.launch { observeMedicinesItems() }
    }

    fun filter(query: String?) {
        val filteredList = medicinesListState.filter { medicine ->
            medicine.name.contains(
                query ?: "",
                ignoreCase = true
            )
        }
        filteredMedicinesListState = filteredList
        filterMedicines(filteredMedicinesListState)
    }

    private suspend fun observeMedicinesItems() {
        medicinesRepository.getAllMedicines()
            .onStart { isLoading = true }
            .collect { medicinesList ->
                medicinesListState = medicinesList
                filteredMedicinesListState = medicinesList
                filterMedicines(medicinesList)
                isLoading = false
            }
    }

    private fun filterMedicines(medicinesList: List<Medicine>) {
        freshMedicinesListState = medicinesList.filter { medicine ->
            currentTimeSeconds < medicine.useUntil
        }
        expiredMedicinesListState = medicinesList.filter { medicine ->
            currentTimeSeconds >= medicine.useUntil
        }
    }
}