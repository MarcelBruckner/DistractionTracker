package org.brucknem.distractiontracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.firebase.ui.auth.util.ExtraConstants
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName( /* yourPackageName= */
                "org.brucknem.distractiontracker",  /* installIfNotAvailable= */
                true,  /* minimumVersion= */
                null
            )
            .setHandleCodeInApp(true) // This must be set to true
            .setUrl("https://google.com") // This URL needs to be whitelisted
            .build()

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.EmailBuilder().enableEmailLinkSignIn().setActionCodeSettings(
//                actionCodeSettings
//            ).build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )
//
//        if (AuthUI.canHandleIntent(intent)) {
//            val extras = intent.extras ?: return
//            val link = extras.getString(ExtraConstants.EMAIL_LINK_SIGN_IN)
//            if (link != null) {
//                val signInIntent = AuthUI.getInstance()
//                    .createSignInIntentBuilder()
//                    .setEmailLink(link)
//                    .setAvailableProviders(providers)
//                    .build()
//                signInLauncher.launch(signInIntent)
//            }
//        }

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
        }
        finish()
    }
}