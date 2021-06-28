package org.brucknem.distractiontracker.data

import androidx.lifecycle.LiveData

class EntryRepository private constructor(
    private val entryDao: DatabaseAccessObject
) {

    fun addEntry(entry: Entry) = entryDao.addEntry(entry)

    fun updateEntry(entry: Entry) = entryDao.updateEntry(entry)

    fun getEntries(): LiveData<List<Entry>> = entryDao.getEntries()

    fun deleteEntry(entryId: Long) = entryDao.deleteEntry(entryId)

    fun reloadDatabase() = entryDao.reloadDatabase()

    fun clear() = entryDao.clear()

    companion object {
        @Volatile
        private var instance: EntryRepository? = null

        fun getInstance(entryDao: DatabaseAccessObject) = instance ?: synchronized(this) {
            instance ?: EntryRepository(entryDao).also { instance = it }
        }
    }
}