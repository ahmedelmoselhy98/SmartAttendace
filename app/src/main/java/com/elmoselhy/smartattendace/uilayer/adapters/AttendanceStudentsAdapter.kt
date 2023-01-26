package com.elmoselhy.smartattendace.uilayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmoselhy.smartattendace.databinding.ItemLayoutAttendanceStudentBinding
import com.elmoselhy.smartattendace.databinding.ItemLayoutMyStudentBinding
import com.elmoselhy.smartattendace.datalayer.models.AttendanceModel
import com.elmoselhy.smartattendace.datalayer.models.BaseModel
import com.elmoselhy.smartattendace.datalayer.models.DoctorModel
import com.elmoselhy.smartattendace.datalayer.models.StudentModel

class AttendanceStudentsAdapter(
    var itemsList: ArrayList<AttendanceModel>
) :
    BaseAdapter<AttendanceStudentsAdapter.CustomViewHolder, AttendanceModel>(itemsList) {
    //Custom View Holder
    open class CustomViewHolder(var binding: ItemLayoutAttendanceStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    //onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = ItemLayoutAttendanceStudentBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    //onBindViewHolder
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        setUpData(holder, position)
        setUpActions(holder, position)
    }

    private fun setUpData(holder: CustomViewHolder, position: Int) {
        holder.binding.run {
            tvStudentName.text = itemsList[position].student!!.fullName
            tvStudentCode.text = itemsList[position].student!!.studentCode
        }
    }

    private fun setUpActions(holder: CustomViewHolder, position: Int) {
    }

}