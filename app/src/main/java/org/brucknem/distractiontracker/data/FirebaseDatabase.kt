package org.brucknem.distractiontracker.data

import com.google.firebase.auth.FirebaseUser

class FirebaseDatabase private constructor(
    user: FirebaseUser
) {
    var entryDao = FirebaseDao(user)
        private set

    companion object {
        @Volatile
        private var instance: FirebaseDatabase? = null

        fun getInstance(user: FirebaseUser) = instance ?: synchronized(this) {
            instance ?: FirebaseDatabase(user).also { instance = it }
        }
    }
}