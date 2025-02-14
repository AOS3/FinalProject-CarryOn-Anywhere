package com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts

// 어플 터져서 일단 보류.
// 모델이랑 뷰모델이랑 다 손봐야함.
class UserModel {
    var customerDocumentId: String = ""
    var customerUserId: String = ""
    var customerUserPw: String = ""
    var customerUserName: String = ""
    var customerUserPhoneNumber: String = ""
    var customerUserProfileImage: String = ""
    // var customerUserState = UserState.USER_STATE_NORMAL
    var customerUserAdvAgree : Boolean = false
    var customerPersonInfoAgree : Boolean = false
    var customerUserAppPushAgree: String = ""
    var fcmToken: String = ""
    var autoLoginToken: String = ""
    var customerUserCreatedAt: Long = 0L
    var customerUserUpdatedAt: Long = 0L
    var isCreator: Boolean = false


}