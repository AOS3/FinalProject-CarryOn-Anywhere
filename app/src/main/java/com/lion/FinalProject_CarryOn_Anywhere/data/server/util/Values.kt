package com.lion.FinalProject_CarryOn_Anywhere.data.server.util

enum class ScreenName{
    // 로그인 화면
    LOGIN_SCREEN,
    // 회원가입 화면
    USER_JOIN_SCREEN,
    // 아이디 찾기 화면
    FIND_ID_SCREEN,
    // 아이디 찾기 완료 화면
    COMPLETED_FIND_ID_SCREEN,
    // 비밀번호 찾기 화면
    FIND_PW_SCREEN,
    // 비밀번호 변경 화면
    CHANGE_PW_SCREEN,
    // 메인 화면
    MAIN_SCREEN,
    // 검색 화면
    PLACE_SEARCH_SCREEN,
    // 장소 상세 정보 화면
    PLACE_INFO_SCREEN,
    WRITE_REQUEST_PLACE,
    TRIP_SEARCH_PLACE,
    SHOW_TRIP_MAP,
    SELECT_TRIP_REGION,
    SELECT_TRIP_DATE,
    EDIT_PLAN_PLACE,
    ADD_TRIP_PLAN,
    // 소셜 화면
    SOCIAL_SCREEN,
    // 후기 화면
    REVIEW_SCREEN,
    // 이야기 화면
    STORY_SCREEN,
    // 글 작성 화면
    POST_SCREEN,
    // 일정 공유 화면
    SHARE_SCREEN,
    // 댓글 화면
    COMMENT_SCREEN,

    // 마이페이지 화면
    MY_PAGE,
    // 계정 설정 화면
    EDIT_MY_INFO,
    // 비밀번호 변경 화면
    EDIT_PW,
    //  나의 글 화면
    MY_POSTS,
    // 내 일정 화면
    MY_TRIP_PLAN,
    // 나의 찜 화면
    MY_LIKE,
    // 이용약관
    DOCUMENT_SCREEN,
    // 개인정보 처리 방침
    DOCUMENT_SCREEN2,

    // 시작 화면 (Splash)
    START_SCREEN

}

// 사용자 상태
enum class UserState(val number:Int, val str:String){
    // 정상
    USER_STATE_NORMAL(1, "정상"),
    // 탈퇴
    USER_STATE_SIGNOUT(2, "탈퇴")
}

// 여행 상태
enum class TripState(val number:Int, val str:String){
    // 정상
    TRIP_STATE_NORMAL(1, "정상"),
    // 삭제
    TRIP_STATE_DELETE(2, "삭제")
}

// 여행 후기 상태
enum class TripReviewState(val number:Int, val str:String){
    // 정상
    TRIP_REVIEW_STATE_NORMAL(1, "정상"),
    // 삭제
    TRIP_REVIEW_STATE_DELETE(2, "삭제")
}

// 여행 게시판 (CarryTalk) 글 상태
enum class CarryTalkState(val number:Int, val str:String){
    // 정상
    CARRYTALK_STATE_NORMAL(1, "정상"),
    // 삭제
    CARRYTALK_STATE_DELETE(2, "삭제")
}

// 여행 게시판 (CarryTalk) 태그 (= 주제)
enum class TalkTag(val number:Int, val str:String){
    // 전체 (목록 보여줄 때 사용)
    TALK_TAG_ALL(0, "전체"),
    // 맛집
    TALK_TAG_RESTAURANT(1, "맛집"),
    // 숙소
    TALK_TAG_ACCOMMODATION(2, "숙소"),
    // 여행 일정
    TALK_TAG_TRIP_PLAN(3, "여행 일정"),
    // 모임
    TALK_TAG_MEET(4, "모임"),
}

// 댓글 상태
enum class ReplyState(val number:Int, val str:String){
    // 정상
    REPLY_STATE_NORMAL(1, "정상"),
    // 삭제
    REPLY_STATE_DELETE(2, "삭제"),
    // 신고
    REPLY_STATE_COMPLAINT(3, "신고"),
    // 수정
    REPLY_STATE_MODIFY(4, "수정"),
}

// 댓글 상태
enum class RequestState(val number:Int, val str:String){
    // 활성화
    REQUEST_STATE_ENABLE(1, "활성화"),
    // 비활성화
    REQUEST_STATE_DISABLE(2, "비활성화"),
}


// 배너 상태
enum class BannerState(val number:Int, val str:String){
    // 활성화
    BANNER_STATE_ENABLE(1, "활성화"),
    // 비활성화
    BANNER_STATE_DISABLE(2, "비활성화"),
}

// 로그인 결과 값
enum class LoginResult(val number: Int, val str: String) {
    LOGIN_RESULT_SUCCESS(1, "로그인 성공"),
    LOGIN_RESULT_ID_NOT_EXIST(2, "존재하지 않는 아이디"),
    LOGIN_RESULT_PASSWORD_INCORRECT(3, "잘못된 비밀번호"),
    LOGIN_RESULT_SIGN_OUT_MEMBER(4, "탈퇴한 회원"),
}

// 앱 푸쉬 알람 동의 여부
enum class AppPushState(val number:Int, val str:String){
    // 활성화
    APP_PUSH_ENABLE(1, "동의"),
    // 비활성화
    APP_PUSH_DISABLE(2, "미동의"),
}

