package com.elmoselhy.smartattendace.uilayer.activities

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmoselhy.smartattendace.uilayer.adapters.DoctorsAdapter
import com.elmoselhy.smartattendace.databinding.ActivityDoctorsBinding
import com.elmoselhy.smartattendace.datalayer.models.BaseModel

class DoctorsActivity : BaseActivity() {
    lateinit var binding: ActivityDoctorsBinding
    override fun setUpLayoutView(): View {
        binding = ActivityDoctorsBinding.inflate(layoutInflater)
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
        var adapter = DoctorsAdapter(list, onItemClicked = {position,item->

        })
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }
}