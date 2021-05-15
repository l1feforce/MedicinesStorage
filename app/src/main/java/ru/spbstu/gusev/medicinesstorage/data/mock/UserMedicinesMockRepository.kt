package ru.spbstu.gusev.medicinesstorage.data.mock

import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine

class UserMedicinesMockRepository {

    fun getMedicines(): List<Medicine> {
        return Medicine.generateRandom(10)
    }
}