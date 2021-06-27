package org.brucknem.distractiontracker.util

import com.google.firebase.auth.FirebaseUser
import org.brucknem.distractiontracker.data.EntryRepository
import org.brucknem.distractiontracker.data.FakeDatabase
import org.brucknem.distractiontracker.data.FirebaseDatabase
import org.brucknem.distractiontracker.viewmodel.EntriesViewModelFactory


object InjectorUtils {

    fun provideFakeEntriesViewModelFactory(): EntriesViewModelFactory {
        return EntriesViewModelFactory(
            EntryRepository.getInstance(
                FakeDatabase.getInstance().entryDao
            )
        )
    }

    fun provideFirebaseEntriesViewModelFactory(): EntriesViewModelFactory {
        return EntriesViewModelFactory(
            EntryRepository.getInstance(
                FirebaseDatabase.getInstance().entryDao
            )
        )
    }

}