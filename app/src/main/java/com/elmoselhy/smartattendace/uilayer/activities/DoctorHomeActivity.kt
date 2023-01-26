package com.elmoselhy.smartattendace.uilayer.activities

import android.R
import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.view.GravityCompat
import com.elmoselhy.smartattendace.databinding.ActivityDoctorHomeBinding
import com.elmoselhy.smartattendace.datalayer.session.Preference
import net.glxn.qrgen.android.QRCode

class DoctorHomeActivity : BaseActivity() {
    lateinit var binding: ActivityDoctorHomeBinding

    override fun setUpLayoutView(): View {
        binding = ActivityDoctorHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        setUpPageActions()
        setUSerData()
        qrCodeGenerator()
    }

    private fun setUSerData() {
        preference.getUserSession()?.let {
            binding.tvStudentName.text = it.fullName
            binding.tvSubjectName.text = it.subjectName
        }
    }

    private fun qrCodeGenerator() {

        val myBitmap: Bitmap = QRCode.from(preference.getUserSession()!!.id!!).bitmap()
        binding.imageQrCode.setImageBitmap(myBitmap)


//        val qrgEncoder = QRGEncoder(preference.getUserSession()!!.id!!, QRGContents.Type.TEXT)
//        qrgEncoder.colorBlack = Color.BLACK
//        qrgEncoder.colorWhite = Color.WHITE
//        try {
//            // Getting QR-Code as Bitmap
//            var bitmap = qrgEncoder.bitmap
//            // Setting Bitmap to ImageView
//            binding.imageQrCode.setImageBitmap(bitmap)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
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
            startActivity(Intent(this, AttendanceDatesActivity::class.java))
        }

        binding.tvLogout.setOnClickListener {
            preference.logout(object : Preference.SessionCallBack {
                override fun setOnLogout() {
                    startActivity(Intent(this@DoctorHomeActivity, LoginActivity::class.java))
                    finishAffinity()
                }
            })
        }
    }
}