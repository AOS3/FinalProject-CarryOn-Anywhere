package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.google.firebase.Timestamp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.UserState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.TripVO
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.UserVO

class UserModel {
    // 사용자 문서 ID
    var userDocumentId: String = ""

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
    var userTimeStamp = 0L // 데이터 입력 시간 (Firebase Timestamp)

    // 유저 상태 (1: 정상, 2: 탈퇴)
    var userState = UserState.USER_STATE_NORMAL

    // 추가 : 앱 푸시 수신 동의 (false: 미동의, true: 동의)
    var userAppPushAgree: String = "미동의"

    fun toUserVO(): UserVO {
        val userVO = UserVO()
        userVO.userKakaoToken = userKakaoToken
        userVO.userImage = userImage
        userVO.userId = userId
        userVO.userPw = userPw
        userVO.userName = userName
        userVO.userPhoneNumber = userPhoneNumber
        userVO.userLikeList = userLikeList.toMutableList()
        userVO.userTripList = userTripList.toMutableList()
        userVO.userTripReviewList = userTripReviewList.toMutableList()
        userVO.userTalkList = userTalkList.toMutableList()
        userVO.userReplyList = userReplyList.toMutableList()
        userVO.userAutoLoginToken = userAutoLoginToken
        userVO.userTimeStamp = userTimeStamp
        userVO.userState = userState.number
        userVO.userAppPushAgree = userAppPushAgree

        return userVO
    }
}