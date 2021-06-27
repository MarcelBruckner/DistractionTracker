package org.brucknem.distractiontracker.data

import com.google.firebase.auth.FirebaseUser

class FirebaseDatabase private constructor() {
    var entryDao = FirebaseDao()
        private set

    companion object {
        @Volatile
        private var instance: FirebaseDatabase? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FirebaseDatabase().also { instance = it }
        }
    }
}