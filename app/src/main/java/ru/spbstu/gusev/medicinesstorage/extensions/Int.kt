package ru.spbstu.gusev.medicinesstorage.extensions

fun Int.toIntOrZero(): Int {
    return if (this > 0) this
    else 0
}