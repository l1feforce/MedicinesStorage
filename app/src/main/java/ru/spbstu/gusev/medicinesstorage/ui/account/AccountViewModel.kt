package ru.spbstu.gusev.medicinesstorage.ui.account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.local.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.models.AccountDetails
import ru.spbstu.gusev.medicinesstorage.models.Statistics
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event

class AccountViewModel(private val medicinesRepository: MedicinesRepository) : ViewModel() {

    val currentUser = MutableLiveData(Firebase.auth.currentUser)
    val accountDetails = Transformations.map(currentUser) { firebaseUser ->
        if (firebaseUser != null) {
            AccountDetails(
                firebaseUser.photoUrl.toString(),
                firebaseUser.displayName ?: firebaseUser.email ?: firebaseUser.uid
            )
        } else AccountDetails("", "")
    }
    val isAuthorized = Transformations.map(currentUser) {
        it != null
    }

    val statistics = MutableLiveData(Statistics())

    fun calculateStatistics() {
        viewModelScope.launch(Dispatchers.IO) {
            val stats = medicinesRepository.getStatistics()
            Log.d("test", "calculateStatistics: $stats")
            statistics.postValue(stats)
        }
    }

    val onSignInEvent = MutableLiveData<Event<Unit>>()
    fun onSignIn() {
        onSignInEvent.value = Event(Unit)
    }

    fun insertMedicinesToRemote() {
        viewModelScope.launch {
            medicinesRepository.getAllLocalMedicines().collect {
                Log.d("test", "onSignIn: medicines: $it")
                medicinesRepository.insertMedicinesToRemote(*it.toTypedArray())
            }
        }
    }

    val onSignOutEvent = MutableLiveData<Event<Unit>>()
    fun onSignOut() {
        onSignOutEvent.value = Event(Unit)
        viewModelScope.launch(Dispatchers.IO) {
            medicinesRepository.deleteAllLocalMedicines()
        }
    }
}