package com.elmoselhy.smartattendace.uilayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmoselhy.smartattendace.databinding.ItemLayoutMyStudentBinding
import com.elmoselhy.smartattendace.datalayer.models.BaseModel

class MyStudentsAdapter(
    var itemsList: ArrayList<BaseModel>,
    private val onItemClicked: (Int, BaseModel) -> Unit,
) :
    BaseAdapter<MyStudentsAdapter.CustomViewHolder, BaseModel>(itemsList) {
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
    }

    private fun setUpActions(holder: CustomViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClicked(position, itemsList[position])
        }
    }

}