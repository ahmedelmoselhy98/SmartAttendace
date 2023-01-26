package com.elmoselhy.smartattendace.uilayer.activities

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmoselhy.smartattendace.uilayer.adapters.DoctorsAdapter
import com.elmoselhy.smartattendace.databinding.ActivityDoctorsBinding
import com.elmoselhy.smartattendace.datalayer.models.AttendanceModel
import com.elmoselhy.smartattendace.datalayer.models.DoctorModel
import com.elmoselhy.smartattendace.datalayer.models.StudentModel
import com.elmoselhy.smartattendace.utilitiess.utils.Utils
import java.util.Date
import kotlin.collections.ArrayList

class DoctorsActivity : BaseActivity() {
    lateinit var binding: ActivityDoctorsBinding
    override fun setUpLayoutView(): View {
        binding = ActivityDoctorsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        firebaseController.observeLoading().observe(this, Observer {
            binding.loading = it
        })
        firebaseController.getDoctors {
            if (it.isNullOrEmpty()) {
                binding.recycler.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
                return@getDoctors
            }
            binding.tvEmpty.visibility = View.GONE
            setUpList(it)
            binding.recycler.visibility = View.VISIBLE
        }
    }

    private fun setUpList(list: ArrayList<DoctorModel>) {
        var adapter = DoctorsAdapter(list, onRegisterClicked = { position, item ->
            firebaseController.registerStudentToDoctor(
                item.id!!,
                preference.getUserSession()!!,
                onResult = {
                    if (it) {
                        Toast.makeText(this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show()
                    }
                })
        }, onDeleteClicked = { position, item ->
            firebaseController.removeStudentFromDoctor(
                item.id!!,
                preference.getUserSession()!!.id!!,
                onResult = {
                    if (it) {
                        Toast.makeText(this, "تم إلغاء التسجيل بنجاح", Toast.LENGTH_SHORT).show()
                    }
                })
        })
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }
}