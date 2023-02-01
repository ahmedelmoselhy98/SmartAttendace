package com.elmoselhy.smartattendace.datalayer.models

open class AttendanceModel : BaseModel() {
    var date: String? = null
    var student: StudentModel? = null
    var doctor: DoctorModel? = null
}