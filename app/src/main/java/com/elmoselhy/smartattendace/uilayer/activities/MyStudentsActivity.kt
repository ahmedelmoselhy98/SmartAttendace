package com.elmoselhy.smartattendace.uilayer.activities

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmoselhy.smartattendace.uilayer.adapters.MyStudentsAdapter
import com.elmoselhy.smartattendace.databinding.ActivityMyStudentsBinding
import com.elmoselhy.smartattendace.datalayer.models.AttendanceModel
import com.elmoselhy.smartattendace.datalayer.models.BaseModel
import com.elmoselhy.smartattendace.datalayer.models.DoctorModel
import com.elmoselhy.smartattendace.datalayer.models.StudentModel
import com.elmoselhy.smartattendace.uilayer.adapters.DoctorsAdapter
import com.elmoselhy.smartattendace.utilitiess.utils.Utils
import java.util.Date
import kotlin.collections.ArrayList

class MyStudentsActivity : BaseActivity() {
    lateinit var binding: ActivityMyStudentsBinding
    override fun setUpLayoutView(): View {
        binding = ActivityMyStudentsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        firebaseController.observeLoading().observe(this, Observer {
            binding.loading = it
        })
        firebaseController.getMyStudents(preference.getUserSession()!!) {
            if (it.isNullOrEmpty()) {
                binding.recycler.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
                return@getMyStudents
            }
            binding.tvEmpty.visibility = View.GONE
            setUpList(it)
            binding.recycler.visibility = View.VISIBLE
        }
    }

    private fun setUpList(list: ArrayList<StudentModel>) {
        var adapter = MyStudentsAdapter(list, onAttendanceClicked = { position, item ->
            var attendanceModel = AttendanceModel()
            attendanceModel.date = Utils.formatDate(Date())
            var studentModel = StudentModel()
            studentModel.id = item.id!!
            studentModel.fullName = item.fullName!!
            studentModel.studentCode = item.studentCode!!
            attendanceModel.student = studentModel
            firebaseController.setStudentAttendance(
                preference.getUserSession()!!.id!!,
                attendanceModel,
                onResult = {
                    if (it) {
                        Toast.makeText(this, "تعيين حضور", Toast.LENGTH_SHORT).show()
                    }
                })
        }, onAbsenceClicked = { position, item ->
            firebaseController.setStudentAbsence(
                preference.getUserSession()!!.id!!,
                item.id!!,
                onResult = {
                    if (it) {
                        Toast.makeText(this, "تعيين غياب", Toast.LENGTH_SHORT).show()
                    }
                })
        })
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }
}