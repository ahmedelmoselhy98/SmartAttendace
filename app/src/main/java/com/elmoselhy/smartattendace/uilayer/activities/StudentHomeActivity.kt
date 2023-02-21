package com.elmoselhy.smartattendace.uilayer.activities

import android.app.PendingIntent
import android.content.ClipDescription
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.elmoselhy.smartattendace.R
import com.elmoselhy.smartattendace.databinding.ActivityStudentHomeBinding
import com.elmoselhy.smartattendace.datalayer.models.AttendanceModel
import com.elmoselhy.smartattendace.datalayer.models.DoctorModel
import com.elmoselhy.smartattendace.datalayer.models.StudentModel
import com.elmoselhy.smartattendace.datalayer.session.Preference
import com.elmoselhy.smartattendace.utilitiess.utils.NfcUtilities
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
        binding.tvSubjectsList.setOnClickListener {
            startActivity(Intent(this, MyDoctorsActivity::class.java))
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
                setMeAttendance(result.content.rawValue)
            }
            QRResult.QRUserCanceled -> Toast.makeText(this, "تم الإلفاء", Toast.LENGTH_SHORT).show()
            QRResult.QRMissingPermission -> Toast.makeText(
                this,
                "Missing permission",
                Toast.LENGTH_SHORT
            ).show()
            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
        }
    }

    private fun setMeAttendance(doctorId: String) {
        firebaseController.getUser(doctorId, onResult = {
            it?.let {
                var attendanceModel = AttendanceModel()
                attendanceModel.date = Utils.formatDate(Date())
                var doctorModel = DoctorModel()
                doctorModel.id = it!!.id!!
                doctorModel.fullName = it.fullName!!
                doctorModel.subjectName = it.subjectName!!
                attendanceModel.doctor = doctorModel
                var studentModel = StudentModel()
                studentModel.id = preference.getUserSession()!!.id!!
                studentModel.fullName = preference.getUserSession()!!.fullName!!
                studentModel.studentCode = preference.getUserSession()!!.studentCode!!
                attendanceModel.student = studentModel
                firebaseController.setStudentAttendance(
                    doctorId,
                    attendanceModel,
                    onResult = { success ->
                        if (success) {
                            Toast.makeText(this, "تعيين حضور", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        })
    }


    override fun onResume() {
        super.onResume()
        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        receiveMessageFromDevice(intent)
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // also reading NFC message from here in case this activity is already started in order
        // not to start another instance of this activity
        receiveMessageFromDevice(intent)
    }

    private fun receiveMessageFromDevice(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables) {
                this?.let {
                    val inNdefMessage = it[0] as NdefMessage
                    val inNdefRecords = inNdefMessage.records
                    val ndefRecord_0 = inNdefRecords[0]
                    val inMessage = String(ndefRecord_0.payload)
                    setMeAttendance(inMessage)
                }
            }
        }
    }
}