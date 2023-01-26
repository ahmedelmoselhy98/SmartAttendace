package com.elmoselhy.smartattendace.uilayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmoselhy.smartattendace.databinding.ItemLayoutAttendanceDateBinding

class AttendanceDateAdapter(
    var itemsList: List<String>,
    private val onItemClick: (Int, String) -> Unit
) :
    BaseAdapter<AttendanceDateAdapter.CustomViewHolder, String>(itemsList) {
    //Custom View Holder
    open class CustomViewHolder(var binding: ItemLayoutAttendanceDateBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    //onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = ItemLayoutAttendanceDateBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    //onBindViewHolder
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        setUpData(holder, position)
        setUpActions(holder, position)
    }

    private fun setUpData(holder: CustomViewHolder, position: Int) {
        holder.binding.run {
            tvDate.text = itemsList[position]
        }
    }

    private fun setUpActions(holder: CustomViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClick(position, itemsList[position])
        }
    }

}