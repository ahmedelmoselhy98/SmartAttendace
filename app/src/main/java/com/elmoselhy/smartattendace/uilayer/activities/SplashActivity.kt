package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.elmoselhy.smartattendace.R
import com.elmoselhy.smartattendace.databinding.ActivityDoctorsBinding
import com.elmoselhy.smartattendace.databinding.ActivitySplashBinding
import com.elmoselhy.smartattendace.utilitiess.enums.UserType
import com.elmoselhy.smartattendace.utilitiess.utils.Utils

class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun setUpLayoutView(): View {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        Utils.executeDelay(2000, onExecute = {
            if (preference.getUserSession() != null) {
                preference.getUserSession()?.let {
                    if (it.userType == UserType.Student.value)
                        startActivity(Intent(this, StudentHomeActivity::class.java))
                    else startActivity(Intent(this, DoctorHomeActivity::class.java))
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        })
    }
}