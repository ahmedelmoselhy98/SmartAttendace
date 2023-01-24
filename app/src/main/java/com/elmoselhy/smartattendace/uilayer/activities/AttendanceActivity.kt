package com.elmoselhy.smartattendace.uilayer.activities

import android.view.View
import com.elmoselhy.smartattendace.databinding.ActivityAttendanceBinding

class AttendanceActivity : BaseActivity() {
    lateinit var binding : ActivityAttendanceBinding

    override fun setUpLayoutView(): View {
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
    }
}