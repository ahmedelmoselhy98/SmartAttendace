package com.elmoselhy.smartattendace.uilayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmoselhy.smartattendace.databinding.ItemLayoutMyStudentBinding
import com.elmoselhy.smartattendace.datalayer.models.BaseModel
import com.elmoselhy.smartattendace.datalayer.models.DoctorModel
import com.elmoselhy.smartattendace.datalayer.models.StudentModel

class MyStudentsAdapter(
    var itemsList: ArrayList<StudentModel>,
    private val onAttendanceClicked: (Int, StudentModel) -> Unit,
    private val onAbsenceClicked: (Int, StudentModel) -> Unit
) :
    BaseAdapter<MyStudentsAdapter.CustomViewHolder, StudentModel>(itemsList) {
    //Custom View Holder
    open class CustomViewHolder(var binding: ItemLayoutMyStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    //onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = ItemLayoutMyStudentBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    //onBindViewHolder
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        setUpData(holder, position)
        setUpActions(holder, position)
    }

    private fun setUpData(holder: CustomViewHolder, position: Int) {
        holder.binding.run {
            tvStudentName.text = itemsList[position].fullName
            tvStudentCode.text = itemsList[position].studentCode
        }
    }

    private fun setUpActions(holder: CustomViewHolder, position: Int) {
        holder.binding.run {
            btnAttendance.setOnClickListener {
                onAttendanceClicked(position, itemsList[position])
            }
            btnAbsence.setOnClickListener {
                onAbsenceClicked(position, itemsList[position])
            }
        }
    }

}