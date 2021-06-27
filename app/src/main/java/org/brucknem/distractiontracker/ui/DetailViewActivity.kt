package org.brucknem.distractiontracker.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.brucknem.distractiontracker.R
import org.brucknem.distractiontracker.data.Entry
import org.brucknem.distractiontracker.data.FirebaseDao
import org.brucknem.distractiontracker.databinding.ActivityDetailViewBinding
import org.brucknem.distractiontracker.util.InjectorUtils
import org.brucknem.distractiontracker.util.UserProvider
import org.brucknem.distractiontracker.viewmodel.EntriesViewModel
import java.text.SimpleDateFormat

class DetailViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailViewBinding
    private lateinit var user: FirebaseUser
    private lateinit var viewModel: EntriesViewModel

    private val imageUrl = "https://cdn2.thecatapi.com/images/c2r.jpg"
    private var dateFormat: java.text.DateFormat = SimpleDateFormat.getDateTimeInstance()

    private lateinit var entry: Entry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: called")

        binding = ActivityDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        user = UserProvider.checkUserLoggedIn(this) ?: return
        viewModel =
            ViewModelProvider(this, InjectorUtils.provideFirebaseEntriesViewModelFactory(user)).get(
                EntriesViewModel::class.java
            )

        getIncomingIntent()

        findViewById<Button>(R.id.delete_entry_btn).setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser ?: return@setOnClickListener

            val db = Firebase.firestore
            db.collection("users").document(user.uid)
                .collection("entries").document(entry.id.toString())
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    Toast.makeText(this, "Successfully deleted entry", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error deleting document", e)
                    Toast.makeText(this, "Error deleting entry", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents")

        val entryId = intent.getLongExtra("entryId", -1)
        if (entryId < 0) {
            entry = Entry()
            setEntry()
            addEntry()
            return
        }

        viewModel.getEntries().observe(this, { entries ->
            run {
                entries.forEach {
                    if (it.id == entryId) {
                        entry = it
                        return@forEach
                    }
                }
                setEntry()
            }
        })
    }

    private fun setEntry() {
        Log.d(TAG, "setImage: setting $entry")

        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(findViewById(R.id.detail_image))

        binding.datetimeDetailView.text = dateFormat.format(entry.datetime)
        binding.distractionDetailView.text = entry.distraction
        binding.howFeelingDetailView.text = entry.howFeeling
        binding.triggerDetailView.text =
            if (entry.internal) "Internal" else "External"
        binding.planningProblemDetailView.text = entry.planningProblem
        binding.ideasDetailView.text = entry.ideas
    }

    private fun addEntry() {
        viewModel.addEntry(entry)
    }

    companion object {
        private const val TAG = "DetailView"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}