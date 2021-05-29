package ru.spbstu.gusev.medicinesstorage.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spbstu.gusev.medicinesstorage.models.AccountDetails
import ru.spbstu.gusev.medicinesstorage.models.Statistics

class AccountViewModel : ViewModel() {

    val accountDetails = MutableLiveData<AccountDetails>(AccountDetails("", "", Statistics(0, 0)))
    val isAuthorized = MutableLiveData<Boolean>(false)

    fun onSignIn() {
        //TODO()
    }

    fun onSignOut() {
        //TODO()
    }
}