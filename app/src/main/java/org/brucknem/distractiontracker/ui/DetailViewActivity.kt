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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.brucknem.distractiontracker.R
import org.brucknem.distractiontracker.data.Entry
import org.brucknem.distractiontracker.databinding.ActivityDetailViewBinding
import org.brucknem.distractiontracker.util.InjectorUtils
import org.brucknem.distractiontracker.viewmodel.EntriesViewModel
import java.text.SimpleDateFormat

class DetailViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailViewBinding

    private val imageUrl = "https://cdn2.thecatapi.com/images/c2r.jpg"
    private var dateFormat: java.text.DateFormat = SimpleDateFormat.getDateTimeInstance()

    private lateinit var entry: Entry
    private lateinit var entryId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: called")

        binding = ActivityDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getIncomingIntent()

        findViewById<Button>(R.id.delete_entry_btn).setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser ?: return@setOnClickListener

            val db = Firebase.firestore
            db.collection("users").document(user.uid)
                .collection("entries").document(entryId)
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

        val position = intent.getIntExtra("entryPosition", -1)
        if (position < 0) {
            finish()
        }

        val viewModel =
            ViewModelProvider(this, InjectorUtils.provideEntriesViewModelFactory()).get(
                EntriesViewModel::class.java
            )

        viewModel.getEntries().observe(this, { entries ->
            run {
                entry = entries[position]
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

        findViewById<TextView>(R.id.datetime_detail_view).text = dateFormat.format(entry.datetime)
        findViewById<TextView>(R.id.distraction_detail_view).text = entry.distraction
        findViewById<TextView>(R.id.how_feeling_detail_view).text = entry.howFeeling
        findViewById<TextView>(R.id.trigger_detail_view).text =
            if (entry.internal) "Internal" else "External"
        findViewById<TextView>(R.id.planning_problem_detail_view).text = entry.planningProblem
        findViewById<TextView>(R.id.ideas_detail_view).text = entry.ideas
    }

    companion object {
        private const val TAG = "DetailView"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}