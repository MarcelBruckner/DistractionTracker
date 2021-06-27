package org.brucknem.distractiontracker.ui

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
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
import java.time.LocalDateTime
import java.util.*


class DetailViewActivity : AppCompatActivity(), DateTimePicker.OnDateTimeSelectedListener,
    TextWatcher, RadioGroup.OnCheckedChangeListener {
    private lateinit var binding: ActivityDetailViewBinding
    private lateinit var user: FirebaseUser
    private lateinit var viewModel: EntriesViewModel

    private val imageUrl = "https://cdn2.thecatapi.com/images/c2r.jpg"

    private lateinit var entry: Entry

    private lateinit var dateTimePicker: DateTimePicker

    private var lastUpdate = Calendar.getInstance().timeInMillis

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

        dateTimePicker = DateTimePicker(this, this)

        binding.pickDateTimeBtn.setOnClickListener {
            dateTimePicker.show()
        }
        binding.triggerDetailView.setOnCheckedChangeListener(this)

        binding.distractionDetailView.addTextChangedListener(this)
        binding.howFeelingDetailView.addTextChangedListener(this)
        binding.planningProblemDetailView.addTextChangedListener(this)
        binding.ideasDetailView.addTextChangedListener(this)

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

        binding.entry = entry
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

    override fun onSelected(datetime: Long) {
        entry.datetime = datetime
        addEntry()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        uploadIfLastUploadLongPast()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        uploadIfLastUploadLongPast()
    }

    override fun afterTextChanged(s: Editable?) {
        uploadIfLastUploadLongPast()
    }

    private fun uploadIfLastUploadLongPast() {
        val now = Calendar.getInstance().timeInMillis
        if (now > lastUpdate + 10000) {
            lastUpdate = now
            addEntry()
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val internal: Boolean = (group?.getChildAt(0) as RadioButton).isChecked
        Log.d(TAG, "onCheckedChanged: $internal")

        addEntry()
    }
}
