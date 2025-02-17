package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.google.firebase.Timestamp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.UserState

class UserVO {

    // 사용자 카카오 토큰 값
    var userKakaoToken: String = ""
    // 사용자 프로필 이미지
    var userImage: String = ""
    // 사용자 Id
    var userId: String = ""
    // 사용자 Password
    var userPw: String = ""
    // 이름
    var userName: String = ""
    // 사용자 휴대폰 번호
    var userPhoneNumber: String = ""
    // 사용자 찜 목록
    var userLikeList = mutableListOf<String>()
    // 사용자 여행 목록
    var userTripList = mutableListOf<String>()
    // 작성한 여행 후기 글 목록
    var userTripReviewList = mutableListOf<String>()
    // 작성한 여행 게시판 글 목록 (CarryTalk)
    var userTalkList = mutableListOf<String>()
    // 작성한 댓글 목록 (CarryTalk)
    var userReplyList = mutableListOf<String>()
    // 자동 로그인 토큰 값
    var userAutoLoginToken: String = ""
    // 사용자 계정 생성 시간 (데이터가 들어온 시간)
    var userTimeStamp:Long = 0L
    // 유저 상태 (1: 정상, 2: 탈퇴)
    var userState: Int = 1

    fun toUserModel(userDocumentId:String) : UserModel {
        val userModel = UserModel()

        userModel.userDocumentId = userDocumentId
        userModel.userKakaoToken = userKakaoToken
        userModel.userImage = userImage
        userModel.userId = userId
        userModel.userPw = userPw
        userModel.userName = userName
        userModel.userPhoneNumber = userPhoneNumber
        userModel.userLikeList = userLikeList.toMutableList()
        userModel.userTripList = userTripList.toMutableList()
        userModel.userTripReviewList = userTripReviewList.toMutableList()
        userModel.userTalkList = userTalkList.toMutableList()
        userModel.userReplyList = userReplyList.toMutableList()
        userModel.userAutoLoginToken = userAutoLoginToken
        userModel.userTimeStamp = userTimeStamp

        when(userState){
            UserState.USER_STATE_NORMAL.number -> userModel.userState = UserState.USER_STATE_NORMAL
            UserState.USER_STATE_SIGNOUT.number -> userModel.userState = UserState.USER_STATE_SIGNOUT
        }

        return userModel
    }
}