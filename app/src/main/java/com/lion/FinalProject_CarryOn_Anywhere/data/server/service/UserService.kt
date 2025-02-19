package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.kakao.sdk.user.model.User
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.UserRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.LoginResult
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.UserState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.UserVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserService() {
    companion object {
        // 사용자 정보 추가 메서드
        fun addUserData(userModel: UserModel){
            // 데이터를 VO에 담아준다.
            val userVO = userModel.toUserVO()
            // 저장하는 메서드를 호출한다.
            UserRepository.addUserData(userVO)
        }

        // 입력된 userId가 존재하는지 확인하는 메서드
        suspend fun checkJoinUserId(userId:String) : Boolean{
            // 아이디를 통해 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(userId)
            // 가져온 데이터가 있다면
            if(userVoList.isNotEmpty()){
                return false
            }
            // 가져온 데이터가 없다면
            else {
                return true
            }
        }

        // 사용자 Document Id를 통해 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByUserDocumentIdOne(userDocumentId:String) : UserModel{
            val userVO = UserRepository.selectUserDataByUserDocumentIdOne(userDocumentId)
            val userModel = userVO.toUserModel(userDocumentId)

            return userModel
        }

        // 사용자 ID를 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : UserModel{
            val tempMap = UserRepository.selectUserDataByUserIdOne(userId)
            val loginUserVo = tempMap["user_vo"] as UserVO
            val loginUserDocumentId = tempMap["user_document_id"] as String

            val loginUserModel = loginUserVo.toUserModel(loginUserDocumentId)

            return loginUserModel
        }

        // 로그인 처리 메서드
        suspend fun checkLogin(loginUserId:String, loginUserPw:String) : LoginResult {
            // 로그인 결과
            var result = LoginResult.LOGIN_RESULT_SUCCESS

            // 입력한 아이디로 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(loginUserId)
            // 가져온 사용자 정보가 없다면
            if(userVoList.isEmpty()){
                result = LoginResult.LOGIN_RESULT_ID_NOT_EXIST
            } else {
                if(loginUserPw != userVoList[0].userPw){
                    // 비밀번호가 다르다면
                    result = LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT
                }
                // 탈퇴한 회원 이라면
                if(userVoList[0].userState == UserState.USER_STATE_SIGNOUT.number){
                    result = LoginResult.LOGIN_RESULT_SIGN_OUT_MEMBER
                }
            }
            return result
        }

        // 자동 로그인 토큰값 갱신 메서드
        suspend fun updateUserAutoLoginToken(context: Context, userDocumentId:String){
            // 새로운 토큰 값 발행
            val newToken = "${userDocumentId}${System.nanoTime()}"
            // SharedPreference 에 저장
            val pref = context.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
            pref.edit {
                putString("token", newToken)
            }
            // 서버에 저장
            UserRepository.updateUserAutoLoginToken(userDocumentId, newToken)
        }

        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken:String) : UserModel?{
            val loginMap = UserRepository.selectUserDataByLoginToken(loginToken)
            if(loginMap == null){
                return null
            } else {
                val userDocumentId = loginMap["userDocumentId"] as String
                val userVO = loginMap["userVO"] as UserVO

                val userModel = userVO.toUserModel(userDocumentId)
                return userModel
            }
        }

        // 탈퇴 처리 메서드(해당 유저의 상태값 변경)
        suspend fun updateUserState(userDocumentId:String, newState: UserState){
            UserRepository.updateUserState(userDocumentId,newState)
        }

        // 카카오 로그인 후 Firestore에서 사용자 정보 가져오기
        suspend fun handleKakaoLogin(email: String, userName: String, userProfileImage: String, kakaoToken: String): UserModel? {
            return try {
                withContext(Dispatchers.IO) {
                    val user = UserRepository.getOrCreateUser(email, userName, userProfileImage, kakaoToken)
                    Log.d("test100", "Firestore에서 유저 처리 완료: ${user.userId}")
                    user
                }
            } catch (e: Exception) {
                Log.e("test100", "Firestore 사용자 처리 중 오류 발생", e)
                null
            }
        }

    }
}