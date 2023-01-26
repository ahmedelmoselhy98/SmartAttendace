package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidx.core.view.GravityCompat
import com.elmoselhy.smartattendace.databinding.ActivityStudentHomeBinding
import com.elmoselhy.smartattendace.datalayer.models.AttendanceModel
import com.elmoselhy.smartattendace.datalayer.models.StudentModel
import com.elmoselhy.smartattendace.datalayer.session.Preference
import com.elmoselhy.smartattendace.utilitiess.utils.Utils
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import java.util.*

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
        setUSerData()
    }

    private fun setUSerData() {
        preference.getUserSession()?.let {
            binding.tvStudentName.text = it.fullName
            binding.tvStudentCode.text = it.studentCode
        }
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
        binding.tvLogout.setOnClickListener {
            preference.logout(object : Preference.SessionCallBack {
                override fun setOnLogout() {
                    startActivity(Intent(this@StudentHomeActivity, LoginActivity::class.java))
                    finishAffinity()
                }
            })
        }
        binding.cardScanQr.setOnClickListener {
            scanQrCodeLauncher.launch(null)
        }
    }

    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        // handle QRResult
        when (result) {
            is QRResult.QRSuccess -> {
                var attendanceModel = AttendanceModel()
                attendanceModel.date = Utils.formatDate(Date())
                var studentModel = StudentModel()
                studentModel.id = preference.getUserSession()!!.id!!
                studentModel.fullName = preference.getUserSession()!!.fullName!!
                studentModel.studentCode = preference.getUserSession()!!.studentCode!!
                attendanceModel.student = studentModel
                firebaseController.setStudentAttendance(
                    result.content.rawValue,
                    attendanceModel,
                    onResult = {
                        if (it) {
                            Toast.makeText(this, "تعيين حضور", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
            QRResult.QRUserCanceled -> Toast.makeText(this, "تم الإلفاء", Toast.LENGTH_SHORT).show()
            QRResult.QRMissingPermission -> Toast.makeText(this, "Missing permission", Toast.LENGTH_SHORT).show()
            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
        }
    }
}