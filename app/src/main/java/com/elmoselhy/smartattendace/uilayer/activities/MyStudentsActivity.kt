package com.elmoselhy.smartattendace.uilayer.activities

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmoselhy.smartattendace.uilayer.adapters.MyStudentsAdapter
import com.elmoselhy.smartattendace.databinding.ActivityMyStudentsBinding
import com.elmoselhy.smartattendace.datalayer.models.BaseModel

class MyStudentsActivity : BaseActivity() {
    lateinit var binding: ActivityMyStudentsBinding
    override fun setUpLayoutView(): View {
        binding = ActivityMyStudentsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        setUpList()
    }

    private fun setUpList() {
        var list = ArrayList<BaseModel>()
        list.add(BaseModel())
        list.add(BaseModel())
        list.add(BaseModel())
        list.add(BaseModel())
        list.add(BaseModel())
        list.add(BaseModel())
        list.add(BaseModel())
        var adapter = MyStudentsAdapter(list, onItemClicked = {position,item->

        })
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }
}