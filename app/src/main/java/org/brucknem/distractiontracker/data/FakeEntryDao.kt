package org.brucknem.distractiontracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FakeEntryDao {
    private val entryList = ArrayList<Entry>()
    private val entries = MutableLiveData<ArrayList<Entry>>()

    init {

        entryList.add(
            Entry(
                12345,
                "Yeet",
                "Good",
                true,
                "Idk",
                "More Yeet"
            )
        )

        refresh()
    }

    fun refresh() {
        entries.value = entryList
    }

    fun addEntry(entry: Entry) {
        entryList.add(entry)
        refresh()
    }

    fun getEntries() = entries as LiveData<ArrayList<Entry>>
}