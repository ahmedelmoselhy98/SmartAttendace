package com.elmoselhy.smartattendace.datalayer.models

import com.elmoselhy.smartattendace.utilitiess.enums.UserType

open class UserModel:BaseModel() {
    var email: String? = null
    var fullName: String? = null
    var userType: Int? = UserType.Student.value
    var studentCode: String? = null
    var subjectName: String? = null
}