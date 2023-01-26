package com.elmoselhy.smartattendace.uilayer.activities

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmoselhy.smartattendace.databinding.ActivityAttendanceStudentsBinding
import com.elmoselhy.smartattendace.datalayer.models.AttendanceModel
import com.elmoselhy.smartattendace.uilayer.adapters.AttendanceStudentsAdapter

class AttendanceStudentsActivity : BaseActivity() {
    lateinit var binding: ActivityAttendanceStudentsBinding

    override fun setUpLayoutView(): View {
        binding = ActivityAttendanceStudentsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        firebaseController.observeLoading().observe(this, Observer {
            binding.loading = it
        })
        intent.getStringExtra("date")?.let {
            firebaseController.getAttendanceStudents(
                preference.getUserSession()!!.id!!,
                intent.getStringExtra("date")!!,
                onResult = {
                    if (it.isNullOrEmpty()) {
                        binding.recycler.visibility = View.GONE
                        binding.tvEmpty.visibility = View.VISIBLE
                        return@getAttendanceStudents
                    }
                    binding.tvEmpty.visibility = View.GONE
                    setUpList(it)
                    binding.recycler.visibility = View.VISIBLE
                })
        }
    }

    private fun setUpList(list: ArrayList<AttendanceModel>) {
        var adapter = AttendanceStudentsAdapter(list)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }
}