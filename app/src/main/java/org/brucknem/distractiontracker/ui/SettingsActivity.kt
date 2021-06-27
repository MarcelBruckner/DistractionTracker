package org.brucknem.distractiontracker.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import org.brucknem.distractiontracker.R

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_settings)
        findViewById<Button>(R.id.sign_out).setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    finish()
                }
        }

        findViewById<Button>(R.id.delete_account).setOnClickListener {
            // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
            val builder: AlertDialog.Builder = this.let {
                AlertDialog.Builder(it)
            }

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Do you really want to delete your account? This cannot be undone!")
                .setTitle("Really delete?")
                .apply {
                    setPositiveButton("Cancel") { dialog, _ -> dialog.cancel() }
                    setNegativeButton(
                        "Delete"
                    ) { dialog, _ ->
                        AuthUI.getInstance()
                            .delete(context)
                            .addOnCompleteListener {
                                finish()
                            }
                    }
                }

            // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            val dialog: AlertDialog? = builder.create()
            dialog?.show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}