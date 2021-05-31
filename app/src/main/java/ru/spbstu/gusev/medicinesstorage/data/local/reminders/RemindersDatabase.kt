package ru.spbstu.gusev.medicinesstorage.data.local.reminders

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.TimeConverter
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder

const val REMINDERS_DATABASE_NAME = "reminders_db"

@Database(entities = [ Reminder::class, TriggeredReminder::class], version = 1)
@TypeConverters(TimeConverter::class)
abstract class RemindersDatabase: RoomDatabase() {
    abstract fun remindersDao(): RemindersDao
    abstract fun triggeredRemindersDao(): TriggeredRemindersDao
}