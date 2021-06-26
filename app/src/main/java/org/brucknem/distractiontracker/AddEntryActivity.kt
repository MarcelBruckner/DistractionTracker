package org.brucknem.distractiontracker

import androidx.appcompat.app.AppCompatActivity

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

import java.util.Calendar;

class AddEntryActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    lateinit var textView: TextView
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0
    lateinit var calendar: Calendar

    private var dateFormat: java.text.DateFormat = SimpleDateFormat.getDateTimeInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: called")

        setContentView(R.layout.activity_add_entry)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        calendar = Calendar.getInstance()

        textView = findViewById(R.id.datetime_add_entry)

        myDay = calendar.get(Calendar.DAY_OF_MONTH)
        myMonth = calendar.get(Calendar.MONTH)
        myYear = calendar.get(Calendar.YEAR)
        myHour = calendar.get(Calendar.HOUR) + 12 * calendar.get(Calendar.AM_PM)

        myMinute = calendar.get(Calendar.MINUTE)
        setDateTime()

        findViewById<Button>(R.id.btnPick).setOnClickListener {
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month, day)
            datePickerDialog.show()
        }

        findViewById<Button>(R.id.add_entry_btn).setOnClickListener {
            onAddEntry()
        }

        findViewById<Button>(R.id.cancel_add_entry_btn).setOnClickListener {
            finish()
        }
    }

    private fun onAddEntry() {
        Log.d(TAG, "onAddEntry: clicked")

        val newEntry = Entry(
            datetime = calendar.timeInMillis,
            distraction = findViewById<EditText>(R.id.distraction_add_entry).text.toString(),
            howFeeling = findViewById<EditText>(R.id.how_feeling_add_entry).text.toString(),
            internal = findViewById<RadioButton>(R.id.internal_trigger_add_entry).isActivated,
            planningProblem = findViewById<EditText>(R.id.planning_problem_add_entry).text.toString(),
            ideas = findViewById<EditText>(R.id.ideas_add_entry).text.toString()
        )

        Log.d(TAG, "onAddEntry: $newEntry")

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            finish()
        }

        val db = Firebase.firestore
        user?.let {
            db.collection("users").document(it.uid)
                .collection("entries").add(newEntry)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Successfully added $newEntry with ID: ${documentReference.id}")
                    Toast.makeText(
                        this,
                        "Successfully saved entry",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding $newEntry", e)
                    Toast.makeText(
                        this,
                        "Something went wrong while saving the entry",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR) + 12 * calendar.get(Calendar.AM_PM)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this, this, hour, minute,
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        setDateTime()
    }

    private fun setDateTime() {
        calendar.set(myYear, myMonth, myDay, myHour, myMinute)

        textView.text = dateFormat.format(calendar.timeInMillis)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        private const val TAG = "AddEntryActivity"
    }
}