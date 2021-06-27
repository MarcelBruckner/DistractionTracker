package org.brucknem.distractiontracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.brucknem.distractiontracker.data.EntryRepository


class EntriesViewModelFactory(
    private val entryRepository: EntryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntriesViewModel(entryRepository) as T
    }
}