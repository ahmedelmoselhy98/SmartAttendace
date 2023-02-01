package com.elmoselhy.smartattendace.uilayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmoselhy.smartattendace.databinding.ItemLayoutMyDoctorBinding
import com.elmoselhy.smartattendace.datalayer.models.DoctorModel

class MyDoctorsAdapter(
    var itemsList: List<DoctorModel>,
    private val onAttendanceClicked: (Int, DoctorModel) -> Unit,
    private val onDeleteClicked: (Int, DoctorModel) -> Unit
) :
    BaseAdapter<MyDoctorsAdapter.CustomViewHolder, DoctorModel>(itemsList) {
    //Custom View Holder
    open class CustomViewHolder(var binding: ItemLayoutMyDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    //onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = ItemLayoutMyDoctorBinding.inflate(inflater, parent, false)
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
            tvSubjectName.text = itemsList[position].subjectName
        }
    }

    private fun setUpActions(holder: CustomViewHolder, position: Int) {
        holder.binding.run {
            btnAttendance.setOnClickListener {
                onAttendanceClicked(position, itemsList[position])
            }
            btnDelete.setOnClickListener {
                onDeleteClicked(position, itemsList[position])
            }
        }
    }

}