package org.brucknem.distractiontracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class DatabaseAccessObject {
    protected val entryList = mutableListOf<Entry>()
    private val entries = MutableLiveData<List<Entry>>()

    init {
        refresh()
    }

    fun refresh() {
        entryList.sortBy { it.datetime }
        entryList.reverse()
        entries.value = entryList
    }

    open fun reloadDatabase() {
        refresh()
    }

    open fun addEntry(entry: Entry) {
        entryList.add(entry)
        refresh()
    }

    open fun updateEntry(entry: Entry) {
        val toReplace = entryList.find { it.id == entry.id }
        if (toReplace != null) {
            entryList[entryList.indexOf(toReplace)] = entry
            refresh()
        } else {
            addEntry(entry)
        }
    }

    open fun deleteEntry(entryId: Long) {
        val entry = entryList.find { it.id == entryId } ?: return
        entryList.remove(entry)
        refresh()
    }

    fun clear() {
        entryList.clear()
        refresh()
    }

    open fun getEntries() = entries as LiveData<List<Entry>>
}
