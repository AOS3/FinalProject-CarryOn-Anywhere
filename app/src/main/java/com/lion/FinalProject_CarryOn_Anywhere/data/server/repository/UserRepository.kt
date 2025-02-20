package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.UserState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.UserVO
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID

class UserRepository {
    companion object {
        // 사용자 정보 추가 메서드
        fun addUserData(userVO: UserVO){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            collectionReference.add(userVO)
        }

        // 사용자 Document Id를 통해 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByUserDocumentIdOne(userDocumentId:String) : UserVO {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.document(userDocumentId).get().await()
            val userVO = result.toObject(UserVO::class.java)!!

            return userVO
        }

        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : MutableMap<String, *>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.whereEqualTo("userId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)

            val userMap = mutableMapOf(
                "user_document_id" to result.documents[0].id,
                "user_vo" to userVoList[0]
            )
            return userMap
        }

        // 사용자 ID를 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserId(userId:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.whereEqualTo("userId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }

        // 자동 로그인 토큰값 갱신 메서드
        suspend fun updateUserAutoLoginToken(userDocumentId:String, newToken:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)
            val tokenMap = mapOf(
                "userAutoLoginToken" to newToken
            )
            documentReference.update(tokenMap).await()
        }

        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken:String) : Map<String, *>?{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val resultList = collectionReference.whereEqualTo("userAutoLoginToken", loginToken).get().await()
            val userVOList = resultList.toObjects(UserVO::class.java)
            if(userVOList.isEmpty()){
                return null
            } else {
                val userDocumentId = resultList.documents[0].id
                val returnMap = mapOf(
                    "userDocumentId" to userDocumentId,
                    "userVO" to userVOList[0]
                )
                return returnMap
            }
        }

        // 탈퇴 처리(해당 유저의 상태 값을 변경)
        suspend fun updateUserState(userDocumentId:String, newState: UserState){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)

            val updateMap = mapOf(
                "userState" to newState.number
            )

            documentReference.update(updateMap).await()
        }

        // 카카오 사용자 데이터를 저장하는 메서드
        suspend fun saveKakaoUserToFirebase(userMap: Map<String, *>): Boolean {
            val firestore = FirebaseFirestore.getInstance()
            return try {
                firestore.collection("UserData").add(userMap).await()
                true
            } catch (e: Exception) {
                println("회원가입 실패: ${e.localizedMessage}")
                false
            }
        }

        private val db = FirebaseFirestore.getInstance()

        // Firestore에서 이메일 기반으로 사용자 데이터 조회
        suspend fun getUserByEmail(email: String): UserModel? {
            return try {
                val querySnapshot = db.collection("UserData")
                    .whereEqualTo("userId", email) // userId 기준 검색 (이메일)
                    .limit(1)
                    .get().await()

                if (!querySnapshot.isEmpty) {
                    querySnapshot.documents[0].toObject(UserModel::class.java)
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("KaKaoLoginRepository", "Firestore 사용자 조회 실패", e)
                null
            }
        }

        // Firestore에 새 사용자 저장
        suspend fun createUser(user: UserModel) {
            try {
                val userCollection = db.collection("UserData")

                // 기존 사용자가 있는지 확인 (이메일 기준)
                val querySnapshot = userCollection.whereEqualTo("userId", user.userId).limit(1).get().await()

                val userRef = if (!querySnapshot.isEmpty) {
                    userCollection.document(querySnapshot.documents[0].id) // 기존 사용자 Document 사용
                } else {
                    userCollection.document() // 새로운 사용자일 경우 자동 생성 Document ID 사용
                }

                // 기존 데이터 가져오기
                val documentSnapshot = userRef.get().await()
                val existingData = documentSnapshot.data?.toMutableMap() ?: mutableMapOf() // 기존 데이터 유지

                // 기존 데이터에 userJoinTime이 있으면 유지, 없으면 현재 시간 저장
                val joinTime = existingData["userJoinTime"] as? Long ?: System.currentTimeMillis()

                // 새로운 데이터 추가
                val userMap = mutableMapOf(
                    "userId" to user.userId,
                    "userName" to user.userName,
                    "userProfileImage" to "",
                    "userTimeStamp" to joinTime, // 기존 값 유지
                    "userAutoLoginToken" to user.userAutoLoginToken,
                    "userKakaoToken" to user.userKakaoToken,
                    // 기존 필드 유지
                    "userPw" to (existingData["userPw"] ?: ""),
                    "userPhoneNumber" to (existingData["userPhoneNumber"] ?: ""),
                )

                // Firestore에 기존 데이터 유지 + 새로운 데이터 추가 (`merge = true` 사용)
                userRef.set(userMap, SetOptions.merge()).await()
                Log.d("KakaoLoginRepository", "Firestore 저장 성공 (Document ID: ${userRef.id})")

            } catch (e: Exception) {
                Log.e("KakaoLoginRepository", "Firestore 사용자 저장 실패", e)
            }
        }

        // Firestore에서 사용자 조회 후 없으면 자동 회원가입
        suspend fun getOrCreateUser(email: String, userName: String, userProfileImage: String, kakaoToken: String): UserModel {
            val existingUser = getUserByEmail(email)
            return if (existingUser != null) {
                Log.d("test100", "기존 사용자 Firestore에서 불러옴: ${existingUser.userId}")
                existingUser
            } else {
                val newUser = UserModel().apply {
                    this.userId = email
                    this.userName = userName
                    this.userImage = userProfileImage
                    this.userTimeStamp = System.currentTimeMillis()
                    this.userState = UserState.USER_STATE_NORMAL
                    this.userAutoLoginToken = UUID.randomUUID().toString()
                    this.userKakaoToken = kakaoToken
                }
                createUser(newUser)
                newUser
            }
        }

        // 계정 설정 관련

        // 서버에서 이미지 파일을 삭제한다.
        suspend fun removeImageFile(imageFileName: String) {
            if (imageFileName.isBlank() || imageFileName == "none") {
                // 파일 이름이 비어있거나 기본값("none")인 경우 삭제하지 않음
                Log.d(
                    "RemoveImageFile",
                    "File name is invalid or set to 'none'. No deletion performed."
                )
                return
            }

            try {
                // Firebase Storage의 경로 참조
                val imageReference =
                    FirebaseStorage.getInstance().reference.child("image/$imageFileName")

                // 삭제 요청
                imageReference.delete().await()
                Log.d("RemoveImageFile", "Successfully deleted file: $imageFileName")

            } catch (e: Exception) {
                // 오류 처리: 파일이 존재하지 않는 경우 또는 기타 예외
                if (e is StorageException && e.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    Log.w("RemoveImageFile", "File not found in Firebase Storage: $imageFileName")
                } else {
                    Log.e("RemoveImageFile", "Error deleting file: $imageFileName", e)
                }
            }
        }

        // 이미지 데이터를 서버로 업로드 하는 메서드
        suspend fun uploadImage(sourceFilePath: String, serverFilePath: String) {
            // 저장되어 있는 이미지의 경로
            val file = File(sourceFilePath)
            val fileUri = Uri.fromFile(file)
            // 업로드 한다.
            val firebaseStorage = FirebaseStorage.getInstance()
            val childReference = firebaseStorage.reference.child("image/$serverFilePath")
            childReference.putFile(fileUri).await()
        }

        // 사용자 데이터를 수정한다.
        suspend fun updateUserData(userVO: UserVO, userDocumentId: String) {
            // 수정할 데이터를 담을 맵
            val userMap = mapOf(

                "userImage" to userVO.userImage,
                "userName" to userVO.userName,
                "userAppPushAgree" to userVO.userAppPushAgree,
            )
            // 수정할 문서에 접근할 수 있는 객체를 가져온다.
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)
            documentReference.update(userMap).await()
        }

        // 사용자 비밀번호를 수정한다.
        suspend fun updateUserPwData(userVO: UserVO, userDocumentId: String) {
            // 수정할 데이터를 담을 맵
            val userMap = mapOf(
                "userPw" to userVO.userPw,
            )
            // 수정할 문서에 접근할 수 있는 객체를 가져온다.
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)
            documentReference.update(userMap).await()
        }



    }
}