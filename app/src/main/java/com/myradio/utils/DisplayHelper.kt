package com.myradio.utils

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.util.*

@SuppressLint("ShowToast")
object DisplayHelper {

    fun displayProgressbar(isDisplayed: Boolean, progressBar: ProgressBar) {
        progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    fun displaySnack(error: String, parent: View) {
        Snackbar.make(parent, error, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(Color.RED)
            .show()
    }

    fun displayToast(msg:String,context: Context){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }

    fun showTimePicker(time: TextView, context: Context) {
        val currentTime = Calendar.getInstance()
        val hour = currentTime[Calendar.HOUR_OF_DAY]
        val minute = currentTime[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(context,
            { currentTime, selectedHour, selectedMinute ->
                time.text ="$selectedHour:$selectedMinute"
            }, hour, minute, true
        )
        timePickerDialog.show()
    }
}