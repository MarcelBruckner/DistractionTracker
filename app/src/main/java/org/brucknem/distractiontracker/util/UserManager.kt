package org.brucknem.distractiontracker.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.brucknem.distractiontracker.ui.SignInActivity

object UserManager {

    fun checkUserLoggedIn(context: Context? = null): FirebaseUser? {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null && context != null) {
            val signInIntent = Intent(context, SignInActivity::class.java)
            startActivity(context, signInIntent, null)
        }

        return user
    }

    fun logout(context: Context, onLoggedOutListener: OnLoggedOutListener) {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                onLoggedOutListener.onLoggedOut()
            }
    }

    fun deleteAccount(context: Context, onLoggedOutListener: OnLoggedOutListener) {
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(context)
        }

        builder.setMessage("Do you really want to delete your account? This cannot be undone!")
            .setTitle("Really delete?")
            .apply {
                setPositiveButton("Cancel") { dialog, _ -> dialog.cancel() }
                setNegativeButton(
                    "Delete"
                ) { _, _ ->
                    AuthUI.getInstance()
                        .delete(context)
                        .addOnCompleteListener {
                            onLoggedOutListener.onLoggedOut()
                        }
                }
            }

        val dialog: AlertDialog? = builder.create()
        dialog?.show()
    }

    interface OnLoggedOutListener {
        fun onLoggedOut()
    }
}