package org.brucknem.distractiontracker.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.brucknem.distractiontracker.ui.SignInActivity

object UserProvider {

    fun checkUserLoggedIn(context: Context): FirebaseUser? {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            val signInIntent = Intent(context, SignInActivity::class.java)
            startActivity(context, signInIntent, null)
        }
        return user
    }
}