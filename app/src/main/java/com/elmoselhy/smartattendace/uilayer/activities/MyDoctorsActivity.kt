package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmoselhy.smartattendace.uilayer.adapters.DoctorsAdapter
import com.elmoselhy.smartattendace.databinding.ActivityDoctorsBinding
import com.elmoselhy.smartattendace.databinding.ActivityMyDoctorsBinding
import com.elmoselhy.smartattendace.datalayer.models.AttendanceModel
import com.elmoselhy.smartattendace.datalayer.models.DoctorModel
import com.elmoselhy.smartattendace.datalayer.models.StudentModel
import com.elmoselhy.smartattendace.uilayer.adapters.MyDoctorsAdapter
import com.elmoselhy.smartattendace.utilitiess.utils.Utils
import java.util.Date
import kotlin.collections.ArrayList

class MyDoctorsActivity : BaseActivity() {
    lateinit var binding: ActivityMyDoctorsBinding
    override fun setUpLayoutView(): View {
        binding = ActivityMyDoctorsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        firebaseController.observeLoading().observe(this, Observer {
            binding.loading = it
        })
        firebaseController.getMyDoctors(preference.getUserSession()!!.id!!) {
            if (it.isNullOrEmpty()) {
                binding.recycler.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
                return@getMyDoctors
            }
            binding.tvEmpty.visibility = View.GONE
            setUpList(it)
            binding.recycler.visibility = View.VISIBLE
        }
    }

    private fun setUpList(list: ArrayList<DoctorModel>) {
        var adapter = MyDoctorsAdapter(list, onAttendanceClicked = { position, item ->
            startActivity(
                Intent(
                    this,
                    AttendanceDatesForStudentsActivity::class.java
                ).putExtra("doctorId", item.id)
            )
        }, onDeleteClicked = { position, item ->
            firebaseController.removeStudentFromDoctor(
                item.id!!,
                preference.getUserSession()!!.id!!,
                onResult = {
                    if (it) {
                        Toast.makeText(this, "تم إلغاء التسجيل بنجاح", Toast.LENGTH_SHORT).show()
                        init()
                    }
                })
        })
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }
}