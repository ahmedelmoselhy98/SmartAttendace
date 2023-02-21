package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Intent
import android.graphics.Bitmap
import android.nfc.NfcAdapter
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.elmoselhy.smartattendace.databinding.ActivityDoctorHomeBinding
import com.elmoselhy.smartattendace.datalayer.session.Preference
import com.elmoselhy.smartattendace.utilitiess.utils.NfcUtilities
import net.glxn.qrgen.android.QRCode

class DoctorHomeActivity : BaseActivity() , NfcUtilities.NfcActivity {
    lateinit var binding: ActivityDoctorHomeBinding

    override fun setUpLayoutView(): View {
        binding = ActivityDoctorHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        var nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val isNfcSupported: Boolean = nfcAdapter != null

        if (isNfcSupported) {
            Toast.makeText(this, "Nfc is supported on this device", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT)
            .show()
        var nfcUtilities = NfcUtilities(this)
        nfcAdapter?.setOnNdefPushCompleteCallback(nfcUtilities, this)
        nfcAdapter?.setNdefPushMessageCallback(nfcUtilities, this)

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


    override fun getOutcomingMessage(): String {
        return preference.getUserSession()?.id!!
    }

    override fun signalResult() {
        runOnUiThread {
            Toast.makeText(this, "Nfc Send Success", Toast.LENGTH_SHORT).show()
        }
    }
}