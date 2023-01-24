package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.view.View
import androidx.core.view.GravityCompat
import com.elmoselhy.smartattendace.databinding.ActivityDoctorHomeBinding

class DoctorHomeActivity : BaseActivity() {
    lateinit var binding: ActivityDoctorHomeBinding
    override fun setUpLayoutView(): View {
        binding = ActivityDoctorHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        setUpPageActions()
    }

    private fun setUpPageActions() {
        setUpMenu()
    }

    private fun setUpMenu() {
        binding.ivMenu.setOnClickListener {
            if (binding.drawerLayout.isOpen)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            else binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.tvMyStudentsList.setOnClickListener {
            startActivity(Intent(this, MyStudentsActivity::class.java))
        }
        binding.tvAttendanceList.setOnClickListener {
//            startActivity(Intent(this, AttendanceActivity::class.java))
        }
        binding.tvLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}