package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.elmoselhy.smartattendace.R
import com.elmoselhy.smartattendace.databinding.ActivityLoginBinding
import com.elmoselhy.smartattendace.utilitiess.enums.UserType

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun setUpLayoutView(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        firebaseController.observeLoading().observe(this, Observer {
            binding.loading = it
        })
        setUpPageActions()
    }

    private fun setUpPageActions() {
        binding.linearRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            if (validateInputData()) {
                firebaseController.signInWithEmailAndPassword(binding.inputEmail.editText!!.text.toString(),
                    binding.inputPassword.editText!!.text.toString(),
                    onResult = { userModel ->
                        userModel?.let {
                            it?.let {
                                preference.setUserSession(it)
                                if (it.userType == UserType.Student.value)
                                    startActivity(Intent(this, StudentHomeActivity::class.java))
                                else startActivity(Intent(this, DoctorHomeActivity::class.java))
                            }
                        }
                    })
            }
        }
    }

    private fun validateInputData(): Boolean {
        if (binding.inputEmail.editText!!.text.toString().isNullOrEmpty()) {
            binding.inputEmail.error = getString(R.string.error_message_email)
            return false
        } else if (binding.inputPassword.editText!!.text.toString().isNullOrEmpty()) {
            binding.inputPassword.error = getString(R.string.error_message_password_length)
            return false
        }
        binding.inputEmail.error = null
        binding.inputPassword.error = null
        return true
    }
}