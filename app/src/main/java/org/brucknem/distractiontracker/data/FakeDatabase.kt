package org.brucknem.distractiontracker.data

class FakeDatabase private constructor() {

    var entryDao = FakeEntryDao()
        private set

    companion object {
        @Volatile
        private var instance: FakeDatabase? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FakeDatabase().also { instance = it }
        }
    }
}