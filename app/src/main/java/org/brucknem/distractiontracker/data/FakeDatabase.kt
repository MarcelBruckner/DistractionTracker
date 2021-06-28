package org.brucknem.distractiontracker.data

class FakeDatabase private constructor() {

    var entryDao = FakeEntryDao()
        private set

    init {
        entryDao.addEntry(
            Entry(
                12345,
                "Yeet",
                "Good",
                true,
                false,
                false,
                "More Yeet"
            )
        )
        entryDao.addEntry(
            Entry(
                54321,
                "Mulm",
                "Bad",
                true,
                false,
                false,
                "Less mulm"
            )
        )
    }

    companion object {
        @Volatile
        private var instance: FakeDatabase? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FakeDatabase().also { instance = it }
        }
    }
}