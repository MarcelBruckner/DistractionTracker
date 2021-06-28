package org.brucknem.distractiontracker.viewmodel

import androidx.lifecycle.ViewModel
import org.brucknem.distractiontracker.data.Entry
import org.brucknem.distractiontracker.data.EntryRepository


class EntriesViewModel(
    private val entryRepository: EntryRepository
) : ViewModel() {
    fun getEntries() = entryRepository.getEntries()

    fun addEntry(entry: Entry) = entryRepository.addEntry(entry)

    fun updateEntry(entry: Entry) = entryRepository.updateEntry(entry)

    fun deleteEntry(entryId: Long) = entryRepository.deleteEntry(entryId)

    fun reloadDatabase() = entryRepository.reloadDatabase()

    fun clear() = entryRepository.clear()
}