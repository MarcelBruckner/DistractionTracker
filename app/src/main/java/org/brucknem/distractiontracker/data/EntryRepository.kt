package org.brucknem.distractiontracker.data

class EntryRepository private constructor(
    private val entryDao: FakeEntryDao
) {

    fun addEntry(entry: Entry) {
        entryDao.addEntry(entry)
    }

    fun getEntries() = entryDao.getEntries()

    companion object {
        @Volatile
        private var instance: EntryRepository? = null

        fun getInstance(entryDao: FakeEntryDao) = instance ?: synchronized(this) {
            instance ?: EntryRepository(entryDao).also { instance = it }
        }
    }
}