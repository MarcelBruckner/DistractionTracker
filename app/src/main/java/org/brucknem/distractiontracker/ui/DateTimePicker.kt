package org.brucknem.distractiontracker.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import java.text.SimpleDateFormat
import java.util.*

class DateTimePicker(
    private var context: Context,
    private var listener: OnDateTimeSelectedListener
) : DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

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
    var calendar = Calendar.getInstance()

    fun dateTime() = calendar.timeInMillis

    fun show() {
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(context, this, year, month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR) + 12 * calendar.get(Calendar.AM_PM)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            context, this, hour, minute,
            DateFormat.is24HourFormat(context)
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        calendar.set(myYear, myMonth, myDay, myHour, myMinute)
        listener.onSelected(dateTime())
    }

    interface OnDateTimeSelectedListener {
        fun onSelected(datetime: Long)
    }
}