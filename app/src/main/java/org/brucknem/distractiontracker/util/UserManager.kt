package org.brucknem.distractiontracker.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.brucknem.distractiontracker.ui.MainActivity
import org.brucknem.distractiontracker.viewmodel.EntriesViewModel

object UserManager {

    private const val TAG = "UserManager"

    class LoggedInResultContract : ActivityResultContract<Intent, Boolean>() {
        override fun createIntent(context: Context, input: Intent?): Intent {
            return input!!
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            Log.d(TAG, "parseResult: resultCode $resultCode")
            return true
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
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