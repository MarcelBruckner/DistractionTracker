package org.brucknem.distractiontracker.viewmodel

import androidx.lifecycle.ViewModel
import org.brucknem.distractiontracker.data.Entry
import org.brucknem.distractiontracker.data.EntryRepository


class EntriesViewModel(
    private val entryRepository: EntryRepository
) : ViewModel() {
    fun getEntries() = entryRepository.getEntries()

    fun addEntry(entry: Entry) = entryRepository.addEntry(entry)

    fun deleteEntry(entryId: Long) = entryRepository.deleteEntry(entryId)
}