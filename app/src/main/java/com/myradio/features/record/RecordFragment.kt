package com.myradio.features.record

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myradio.databinding.FragmentRecordBinding

import android.app.TimePickerDialog
import android.widget.TextView
import com.myradio.utils.DisplayHelper
import com.myradio.utils.DisplayHelper.showTimePicker
import java.util.*


private const val TAG = "RecordFragment"

class RecordFragment : Fragment() {

    private lateinit var binding: FragmentRecordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.startTimeTxt.setOnClickListener {
            Log.i(TAG, "startTimeTxt is Clicked")
            showTimePicker(binding.startTimeTxt,requireActivity())
        }

        binding.endTimeTxt.setOnClickListener {
            Log.i(TAG, "endTimeTxt is Clicked")
            showTimePicker(binding.endTimeTxt,requireActivity())
        }


    }



}