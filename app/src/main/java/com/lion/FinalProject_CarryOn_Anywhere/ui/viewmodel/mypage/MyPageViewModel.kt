package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    @ApplicationContext context: Context,
    // val customerService: CustomerService
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 회원 이름 가져오기
    val textFieldNameValue = mutableStateOf(carryOnApplication.loginUserModel.userName)

    val showImage1State = mutableStateOf(false)
    val showImage2State = mutableStateOf(false)
    val showImage3State = mutableStateOf(false)

    // 카메라나 앨범을 통해 가져온 사진을 담을 상태변수
    val imageBitmapState = mutableStateOf<Bitmap?>(null)

    // 서버로 부터 이미지를 받아올 수 있는 Uri를 담을 상태 변수
    val imageUriState = mutableStateOf<Uri?>(null)

    // 회원 프로필 사진 가져오기 -> 없으면 기본값..
    fun loadProfileImage() {
        val profileImage = carryOnApplication.loginUserModel.userImage

        // 값이 비어 있거나 기본값으로 설정된 경우
        if (profileImage.isNullOrEmpty() || profileImage == "none") {
            // 기본 이미지 표시
            showImage1State.value = true
        }
        // 외부 URL인 경우 (카카오 이미지)
        else if (profileImage.startsWith("http://") || profileImage.startsWith("https://")) {
            loadImageFromUrl(profileImage)
        }
        // Firebase Storage 파일 이름인 경우 (기존 이미지)
        else {
            loadImageFromFirebaseStorage(profileImage)
        }
    }

    // 외부 URL에서 이미지 로드
    fun loadImageFromUrl(url: String) {
        try {
            val imageUri = Uri.parse(url) // URL을 URI로 변환
            showImage2State.value = true
            imageUriState.value = imageUri
        } catch (e: Exception) {
            println("URL 이미지 로드 실패: ${e.localizedMessage}")
            showImage1State.value = true // 기본 이미지 표시
        }
    }

    // Firebase Storage에서 이미지 로드
    fun loadImageFromFirebaseStorage(fileName: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("image/$fileName")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            showImage2State.value = true
            imageUriState.value = uri
        }.addOnFailureListener { e ->
            println("Firebase Storage 이미지 로드 실패: ${e.localizedMessage}")
            showImage1State.value = true // 기본 이미지 표시
        }

    }

    fun logoutOnClick(context: Context) {
        viewModelScope.launch {
            try {
                val userDocumentId = carryOnApplication.loginUserModel.userDocumentId
                UserService.clearAutoLoginToken(userDocumentId, context)

                carryOnApplication.isLoggedIn.value = false

                // 네비게이션 처리
                carryOnApplication.navHostController.popBackStack(ScreenName.MY_PAGE.name, inclusive = true)
                carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name)

            } catch (e: Exception) {
                println("로그아웃 처리 실패: ${e.localizedMessage}")
            }
        }
    }




    // 페이지 이동 관련
    // 계정 설정
    fun ClickEditMyInfo() {
        carryOnApplication.navHostController.navigate(ScreenName.EDIT_MY_INFO.name)
    }

    // 내가 쓴 글
    fun ClickMyPosts() {
        carryOnApplication.navHostController.navigate(ScreenName.MY_POSTS.name)
    }

    // 내 일정 보기
    fun ClickMyUserTripList() {
        carryOnApplication.navHostController.navigate(ScreenName.MY_TRIP_PLAN.name)
    }


    // 서비스 이용 약관
    fun ClickDocument() {
        carryOnApplication.navHostController.navigate(ScreenName.DOCUMENT_SCREEN.name)
    }

    // 개인정보 처리 방침
    fun ClickDocument2() {
        carryOnApplication.navHostController.navigate(ScreenName.DOCUMENT_SCREEN2.name)
    }

}