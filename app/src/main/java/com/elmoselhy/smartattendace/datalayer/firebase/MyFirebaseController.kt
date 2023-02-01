package com.elmoselhy.smartattendace.datalayer.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elmoselhy.smartattendace.datalayer.models.AttendanceModel
import com.elmoselhy.smartattendace.datalayer.models.DoctorModel
import com.elmoselhy.smartattendace.datalayer.models.StudentModel
import com.elmoselhy.smartattendace.datalayer.models.UserModel
import com.elmoselhy.smartattendace.utilitiess.enums.UserType
import com.elmoselhy.smartattendace.utilitiess.utils.Utils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyFirebaseController @Inject constructor(@param:ApplicationContext val mContext: Context) {


    /*
      // Firebase
    implementation 'com.google.firebase:firebase-analytics:21.0.0'
    implementation 'com.google.firebase:firebase-database-ktx:20.0.0'
    implementation 'com.google.firebase:firebase-messaging:21.1.0'
    implementation 'com.google.firebase:firebase-crashlytics-ktx:18.2.11'
    implementation 'com.google.firebase:firebase-crashlytics:18.2.11'
    implementation 'com.google.firebase:firebase-auth-ktx:21.0.0'
    */

    val TAG = MyFirebaseController::class.java.name
    var auth = Firebase.auth
    private val currentUser = auth.currentUser
    private val BASE_URL_KEY = "base_url"
    private val USERS = "Users"
    private val MY_DOCTORS = "MyDoctors"
    private val MY_STUDENTS = "MyStudents"
    private val DOCTORS = "Doctors"
    private val ATTENDANCE = "Attendance"
    private val STUDENTS = "Students"
    private val mRootRef = FirebaseDatabase.getInstance().reference
    private val loadingDataLive = MutableLiveData<Boolean>()
    fun observeLoading(): LiveData<Boolean> {
        return loadingDataLive
    }

    fun createUserWithEmailAndPassword(
        userModel: UserModel,
        password: String,
        onResult: (UserModel) -> Unit
    ) {
        loadingDataLive.postValue(true)
        auth.createUserWithEmailAndPassword(userModel.email!!, password)
            .addOnCompleteListener() { task ->
                loadingDataLive.postValue(false)
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    auth.currentUser?.let {
                        userModel.id = it.uid
                        addUser(userModel, onResult = {
                            onResult(userModel)
                        })
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        mContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (UserModel?) -> Unit
    ) {
        loadingDataLive.postValue(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                loadingDataLive.postValue(false)
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    auth.currentUser?.let {
                        getUser(it.uid, onResult = { userModel ->
                            userModel?.let {
                                onResult(userModel)
                            }
                        })
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        mContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    onResult(null)
                }
            }
    }

    fun addUser(userModel: UserModel, onResult: (Boolean) -> Unit) {
        loadingDataLive.postValue(true)
        userModel.id.let { id ->
            mRootRef.child(USERS).child(id!!)
                .setValue(userModel).addOnCompleteListener {
                    loadingDataLive.postValue(false)
                    onResult(it.isSuccessful)
                }
        }
    }

    fun setStudentAttendance(
        doctorId: String,
        attendanceModel: AttendanceModel,
        onResult: (Boolean) -> Unit
    ) {
        loadingDataLive.postValue(true)
        mRootRef.child(ATTENDANCE).child(doctorId).child(attendanceModel.date!!)
            .child(attendanceModel.student!!.id!!)
            .setValue(attendanceModel).addOnCompleteListener {
                loadingDataLive.postValue(false)
                onResult(it.isSuccessful)
            }
    }

    fun setStudentAbsence(
        doctorId: String,
        studentId: String,
        onResult: (Boolean) -> Unit
    ) {
        loadingDataLive.postValue(true)
        mRootRef.child(ATTENDANCE).child(doctorId).child(Utils.formatDate(Date()))
            .child(studentId)
            .removeValue().addOnCompleteListener {
                loadingDataLive.postValue(false)
                onResult(it.isSuccessful)
            }
    }

    fun registerStudentToDoctor(
        doctorModel: DoctorModel,
        userModel: UserModel,
        onResult: (Boolean) -> Unit
    ) {
        loadingDataLive.postValue(true)
        doctorModel.id.let { id ->
            mRootRef.child(MY_DOCTORS).child(userModel.id!!).child(doctorModel.id!!)
                .setValue(doctorModel).addOnCompleteListener {
                    userModel.id.let { id ->
                        mRootRef.child(MY_STUDENTS).child(doctorModel.id!!).child(userModel.id!!)
                            .setValue(userModel).addOnCompleteListener {
                                loadingDataLive.postValue(false)
                                onResult(it.isSuccessful)
                            }
                    }
                }
        }
    }

    fun removeStudentFromDoctor(doctorId: String, studentId: String, onResult: (Boolean) -> Unit) {
        loadingDataLive.postValue(true)
        mRootRef.child(MY_DOCTORS).child(studentId).child(doctorId).removeValue()
            .addOnCompleteListener {
                mRootRef.child(ATTENDANCE).child(doctorId).removeValue()
                mRootRef.child(MY_STUDENTS).child(doctorId).child(studentId).removeValue()
                    .addOnCompleteListener {
                        loadingDataLive.postValue(false)
                        onResult(it.isSuccessful)
                    }
            }

    }

    private fun getUserTypeTable(userType: Int?): String {
        return if (userType == UserType.Doctor.value) DOCTORS else STUDENTS
    }

    fun getUser(id: String, onResult: (UserModel?) -> Unit) {
        loadingDataLive.postValue(true)
        mRootRef.child(USERS).child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    loadingDataLive.postValue(false)
                    onResult(dataSnapshot.getValue(UserModel::class.java))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    loadingDataLive.postValue(false)
                    databaseError.toException().printStackTrace()
                }
            })
    }

    fun getMyStudents(userModel: UserModel, onResult: (ArrayList<StudentModel>) -> Unit) {
        userModel.id.let {
            loadingDataLive.postValue(true)
            var studentsList = ArrayList<StudentModel>()
            mRootRef.child(MY_STUDENTS).child(it!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        loadingDataLive.postValue(false)
                        try {
                            for (studentSnapShot in dataSnapshot.children) {
                                var user =
                                    studentSnapShot.getValue(UserModel::class.java)
                                user?.let {
                                    if (user.userType == UserType.Student.value) {
                                        var studentModel =
                                            studentSnapShot.getValue(StudentModel::class.java)
                                        studentModel?.let {
                                            studentsList.add(studentModel)
                                        }
                                    }
                                    onResult(studentsList)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        loadingDataLive.postValue(false)
                        databaseError.toException().printStackTrace()
                    }
                })
        }
    }

    fun getDoctors(onResult: (ArrayList<DoctorModel>) -> Unit) {
        loadingDataLive.postValue(true)
        var doctorsList = ArrayList<DoctorModel>()
        mRootRef.child(USERS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    loadingDataLive.postValue(false)
                    try {
                        for (doctorSnapShot in dataSnapshot.children) {
                            var user =
                                doctorSnapShot.getValue(UserModel::class.java)
                            user?.let {
                                if (user.userType == UserType.Doctor.value) {

                                    var doctorModel =
                                        doctorSnapShot.getValue(DoctorModel::class.java)
                                    doctorModel?.let {
                                        doctorsList.add(doctorModel)
                                    }
                                }
                                onResult(doctorsList)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    loadingDataLive.postValue(false)
                    databaseError.toException().printStackTrace()
                }
            })
    }

    fun getMyDoctors(id: String, onResult: (ArrayList<DoctorModel>) -> Unit) {
        loadingDataLive.postValue(true)
        var doctorsList = ArrayList<DoctorModel>()
        mRootRef.child(MY_DOCTORS).child(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    loadingDataLive.postValue(false)
                    try {
                        for (doctorSnapShot in dataSnapshot.children) {
                            var doctorModel =
                                doctorSnapShot.getValue(DoctorModel::class.java)
                            doctorModel?.let {
                                doctorsList.add(doctorModel)
                            }
                            onResult(doctorsList)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    loadingDataLive.postValue(false)
                    databaseError.toException().printStackTrace()
                }
            })
    }

    fun getAttendanceDates(doctorId: String, onResult: (ArrayList<String>) -> Unit) {
        loadingDataLive.postValue(true)
        var datesList = ArrayList<String>()
        mRootRef.child(ATTENDANCE).child(doctorId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    loadingDataLive.postValue(false)
                    try {
                        for (attendanceSnapShot in dataSnapshot.children) {
                            attendanceSnapShot?.let { datesList.add(attendanceSnapShot.key!!) }
                        }
                        onResult(datesList)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    loadingDataLive.postValue(false)
                    databaseError.toException().printStackTrace()
                }
            })
    }

    fun getAttendanceDatesForStudents(
        doctorId: String,
        studentId: String,
        onResult: (ArrayList<String>) -> Unit
    ) {
        loadingDataLive.postValue(true)
        var datesList = ArrayList<String>()
        mRootRef.child(ATTENDANCE).child(doctorId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    loadingDataLive.postValue(false)
                    try {
                        for (snapShot in dataSnapshot.children) {
                            if (!mRootRef.child(ATTENDANCE).child(doctorId).child(snapShot.key!!)
                                    .child(studentId).key.isNullOrEmpty()
                            ) {
                                if (mRootRef.child(ATTENDANCE).child(doctorId).child(snapShot.key!!)
                                        .child(studentId).key == studentId
                                ) {
                                    datesList.add(snapShot.key!!)
                                }
                            }
                        }
                        onResult(datesList)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    loadingDataLive.postValue(false)
                    databaseError.toException().printStackTrace()
                }
            })
    }

    fun getAttendanceStudents(
        doctorId: String,
        date: String,
        onResult: (ArrayList<AttendanceModel>) -> Unit
    ) {
        loadingDataLive.postValue(true)
        var attendanceStudentsList = ArrayList<AttendanceModel>()
        mRootRef.child(ATTENDANCE).child(doctorId).child(date)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    loadingDataLive.postValue(false)
                    try {
                        for (snapShot in dataSnapshot.children) {
                            var attendanceModel =
                                snapShot.getValue(AttendanceModel::class.java)
                            attendanceModel?.let {
                                attendanceStudentsList.add(attendanceModel)
                            }
                            onResult(attendanceStudentsList)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    loadingDataLive.postValue(false)
                    databaseError.toException().printStackTrace()
                }
            })
    }

    private fun checkIfUserLogin(currentUser: FirebaseUser?): Boolean {
        return currentUser != null
    }

    fun signOut() {
        Firebase.auth.signOut()
    }
}