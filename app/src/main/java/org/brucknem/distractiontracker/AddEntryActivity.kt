package org.brucknem.distractiontracker

import androidx.appcompat.app.AppCompatActivity

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime

import java.util.Calendar;

class AddEntryActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    lateinit var textView: TextView
    lateinit var button: Button
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

        textView = findViewById(R.id.textView)
        button = findViewById(R.id.btnPick)

        myDay = calendar.get(Calendar.DAY_OF_MONTH)
        myMonth = calendar.get(Calendar.MONTH)
        myYear = calendar.get(Calendar.YEAR)
        myHour = calendar.get(Calendar.HOUR) + 12 * calendar.get(Calendar.AM_PM)

        myMinute = calendar.get(Calendar.MINUTE)
        setDateTime()

        button.setOnClickListener {
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month, day)
            datePickerDialog.show()
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