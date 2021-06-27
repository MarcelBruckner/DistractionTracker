package org.brucknem.distractiontracker.util

import org.brucknem.distractiontracker.data.EntryRepository
import org.brucknem.distractiontracker.data.FakeDatabase
import org.brucknem.distractiontracker.viewmodel.EntriesViewModelFactory


object InjectorUtils {

    fun provideEntriesViewModelFactory(): EntriesViewModelFactory {
        return EntriesViewModelFactory(
            EntryRepository.getInstance(
                FakeDatabase.getInstance().entryDao
            )
        )
    }
}