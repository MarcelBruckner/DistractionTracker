package org.brucknem.distractiontracker.data

import androidx.lifecycle.LiveData

class EntryRepository private constructor(
    private val entryDao: DatabaseAccessObject
) {

    fun addEntry(entry: Entry) {
        entryDao.addEntry(entry)
    }

    fun getEntries(): LiveData<List<Entry>> {
        return entryDao.getEntries()
    }

    companion object {
        @Volatile
        private var instance: EntryRepository? = null

        fun getInstance(entryDao: DatabaseAccessObject) = instance ?: synchronized(this) {
            instance ?: EntryRepository(entryDao).also { instance = it }
        }
    }
}