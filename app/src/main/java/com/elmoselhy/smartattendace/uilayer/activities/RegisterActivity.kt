package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.elmoselhy.smartattendace.R
import com.elmoselhy.smartattendace.databinding.ActivityRegisterBinding
import com.elmoselhy.smartattendace.datalayer.models.UserModel
import com.elmoselhy.smartattendace.utilitiess.enums.UserType

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
        firebaseController.observeLoading().observe(this, Observer {
            binding.loading = it
        })
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
            if (validateInputData())
                register()
        }
    }

    private fun register() {
        var userModel = UserModel()
        userModel.fullName = binding.inputName.editText!!.text.toString()
        userModel.email = binding.inputEmail.editText!!.text.toString()
        when (binding.rgUserType.checkedRadioButtonId) {
            R.id.rb_student -> {
                userModel.userType = UserType.Student.value
                userModel.studentCode = binding.inputStudentCode.editText!!.text.toString()
                firebaseController.createUserWithEmailAndPassword(
                    userModel,
                    binding.inputPassword.editText!!.text.toString(),
                    onResult = { userModel ->
                        userModel?.let { user ->
                            preference.setUserSession(user)
                            startActivity(Intent(this, StudentHomeActivity::class.java))
                        }
                    })
            }
            R.id.rb_doctor -> {
                userModel.userType = UserType.Doctor.value
                userModel.subjectName = binding.inputSubjectName.editText!!.text.toString()
                firebaseController.createUserWithEmailAndPassword(
                    userModel,
                    binding.inputPassword.editText!!.text.toString(),
                    onResult = { userModel ->
                        userModel?.let {
                            preference.setUserSession(it)
                            startActivity(Intent(this, DoctorHomeActivity::class.java))
                        }
                    })
            }
        }
    }

    private fun validateInputData(): Boolean {
        if (binding.inputName.editText!!.text.toString().isNullOrEmpty()) {
            binding.inputName.error = getString(R.string.error_message_required)
            return false
        } else if (binding.inputEmail.editText!!.text.toString().isNullOrEmpty()) {
            binding.inputEmail.error = getString(R.string.error_message_email)
            return false
        } else if (binding.rbStudent.isChecked && binding.inputStudentCode.editText!!.text.toString()
                .isNullOrEmpty()
        ) {
            binding.inputStudentCode.error = getString(R.string.error_message_required)
            return false
        } else if (binding.rbDoctor.isChecked && binding.inputSubjectName.editText!!.text.toString()
                .isNullOrEmpty()
        ) {
            binding.inputSubjectName.error = getString(R.string.error_message_required)
            return false
        } else if (binding.inputPassword.editText!!.text.toString().isNullOrEmpty()) {
            binding.inputPassword.error = getString(R.string.error_message_password_length)
            return false
        }
        binding.inputName.error = null
        binding.inputStudentCode.error = null
        binding.inputSubjectName.error = null
        binding.inputPassword.error = null
        return true
    }
}
