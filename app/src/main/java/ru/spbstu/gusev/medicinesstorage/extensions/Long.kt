package ru.spbstu.gusev.medicinesstorage.extensions

fun Long?.toIntOrZero(): Int {
    if (this == null) return 0
    return try {
        this.toInt()
    } catch (e: Exception) {
        0
    }
}