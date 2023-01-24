package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.view.View
import com.elmoselhy.smartattendace.R
import com.elmoselhy.smartattendace.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun setUpLayoutView(): View {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        setUpPageActions()
    }

    private fun setUpPageActions() {
        binding.tvTitle.setOnClickListener {
            finish()
        }

        binding.rgUserType.setOnCheckedChangeListener { radioGroup, selectedId ->
            when (selectedId) {
                R.id.rb_student -> {
                    binding.inputStudentCode.visibility = View.VISIBLE
                    binding.inputSubjectName.visibility = View.GONE
                }
                R.id.rb_doctor -> {
                    binding.inputStudentCode.visibility = View.GONE
                    binding.inputSubjectName.visibility = View.VISIBLE
                }
            }
        }
        binding.btnRegister.setOnClickListener {
            when (binding.rgUserType.checkedRadioButtonId) {
                R.id.rb_student -> {
                    startActivity(Intent(this, StudentHomeActivity::class.java))
                }
                R.id.rb_doctor -> {
                    startActivity(Intent(this, DoctorHomeActivity::class.java))
                }
            }
        }
    }
}
