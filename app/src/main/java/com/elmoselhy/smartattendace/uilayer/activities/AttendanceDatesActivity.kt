package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmoselhy.smartattendace.databinding.ActivityAttendanceDatesBinding
import com.elmoselhy.smartattendace.uilayer.adapters.AttendanceDateAdapter

class AttendanceDatesActivity : BaseActivity() {
    lateinit var binding: ActivityAttendanceDatesBinding

    override fun setUpLayoutView(): View {
        binding = ActivityAttendanceDatesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        firebaseController.observeLoading().observe(this, Observer {
            binding.loading = it
        })
        firebaseController.getAttendanceDates(preference.getUserSession()!!.id!!, onResult = {
            if (it.isNullOrEmpty()) {
                binding.recycler.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
                return@getAttendanceDates
            }
            binding.tvEmpty.visibility = View.GONE
            setUpList(it)
            binding.recycler.visibility = View.VISIBLE
        })
    }

    private fun setUpList(list: ArrayList<String>) {
        var adapter = AttendanceDateAdapter(list, onItemClick = { position, item ->
            startActivity(Intent(this,AttendanceStudentsActivity::class.java).putExtra("date",item))
        })
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }
}