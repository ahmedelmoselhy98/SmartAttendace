package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.view.View
import com.elmoselhy.smartattendace.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun setUpLayoutView(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        setUpPageActions()
    }

    private fun setUpPageActions() {
        binding.linearRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, StudentHomeActivity::class.java))
        }
    }
}