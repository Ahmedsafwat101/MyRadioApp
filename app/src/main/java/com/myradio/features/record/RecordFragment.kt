package com.myradio.features.record

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myradio.databinding.FragmentRecordBinding

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import androidx.core.content.getSystemService
import com.myradio.utils.DisplayHelper
import com.myradio.utils.DisplayHelper.showTimePicker
import java.util.*
import android.content.Intent
import android.app.PendingIntent
import android.os.Build
import androidx.annotation.RequiresApi


private const val TAG = "RecordFragment"

class RecordFragment : Fragment() {

    private lateinit var binding: FragmentRecordBinding
    private lateinit var alarmManager: AlarmManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.startTimeTxt.setOnClickListener {
            Log.i(TAG, "startTimeTxt is Clicked")
            showTimePicker(binding.startTimeTxt,requireActivity())
        }

        binding.endTimeTxt.setOnClickListener {
            Log.i(TAG, "endTimeTxt is Clicked")
            showTimePicker(binding.endTimeTxt,requireActivity())
        }


        binding.startReminderBtn.setOnClickListener {
            val context = requireActivity()

            val intent = Intent(context,AlarmRadioRecordReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,1000*30,pendingIntent)

        }

        binding.stopReminderBtn.setOnClickListener {

        }

    }



}