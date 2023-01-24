package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.elmoselhy.smartattendace.databinding.ActivityStudentHomeBinding
import io.github.g00fy2.quickie.ScanQRCode

class StudentHomeActivity : BaseActivity() {
    lateinit var binding: ActivityStudentHomeBinding
    override fun setUpLayoutView(): View {
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
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
        binding.tvDoctorsList.setOnClickListener {
            startActivity(Intent(this, DoctorsActivity::class.java))
        }
        binding.cardScanQr.setOnClickListener {
            scanQrCodeLauncher.launch(null)
        }
    }

    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        // handle QRResult
        Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
    }
}