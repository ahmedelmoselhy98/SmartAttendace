package com.elmoselhy.smartattendace.uilayer.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseAdapter<T : ViewHolder, DATA_TYPE>(baseListOfData: List<DATA_TYPE>) :
    RecyclerView.Adapter<T>() {
    // declare properties
    var dataList = ArrayList<DATA_TYPE>()
    var currentSelectedItemPosition = 0

    init {
        dataList.addAll(baseListOfData)
    }

    fun addItem(position: Int, dataModel: DATA_TYPE) {
        dataList[position] = dataModel
    }

    fun replaceDataList(list: List<DATA_TYPE>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun selectItem(position: Int) {
        currentSelectedItemPosition = position
        notifyDataSetChanged()
    }
    fun unSelectItem() {
        currentSelectedItemPosition = -1
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, dataList.size)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}