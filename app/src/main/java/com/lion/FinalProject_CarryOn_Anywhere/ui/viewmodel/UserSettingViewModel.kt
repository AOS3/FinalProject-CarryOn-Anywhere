package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.Tools
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

//data class UserModel(
//    var textFieldModifyIdValue: String
//)


@HiltViewModel
class UserSettingViewModel @Inject constructor(
    @ApplicationContext context: Context,
   // val customerService: CustomerService
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    val textFieldModifyIdValue = mutableStateOf(carryOnApplication.loginCustomerModel.userId)
    val textFieldModifyNameValue = mutableStateOf(carryOnApplication.loginCustomerModel.userName)
    val textFieldModifyPhoneValue = mutableStateOf(carryOnApplication.loginCustomerModel.userPhoneNumber)

    val selectedPushAgree: MutableState<String> =
        mutableStateOf(carryOnApplication.loginCustomerModel.userAppPushAgree)


    // 보여줄 이미지 요소
    val showImage1State = mutableStateOf(false)
    val showImage2State = mutableStateOf(false)
    val showImage3State = mutableStateOf(false)

    // 카메라나 앨범을 통해 가져온 사진을 담을 상태변수
    val imageBitmapState = mutableStateOf<Bitmap?>(null)
    // 서버로 부터 이미지를 받아올 수 있는 Uri를 담을 상태 변수
    val imageUriState = mutableStateOf<Uri?>(null)
    // 서버상에서의 파일 이름
    var newFileName = carryOnApplication.loginCustomerModel.userImage


    // 다이얼로그 제어 변수 -> 유효성 검사시 코드 추가
    val showDialogPwOk = mutableStateOf(false)


    // 회원 탈퇴 다이얼로그 상태 변수
    val showDialogWithdrawalState = mutableStateOf(false)

    // 네비게이션 클릭시 다이얼로그 상태 변수
    val showDialogBackArrowState = mutableStateOf(false)


    // 이미지 URI 리스트
    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> get() = _imageUris





    fun loadProfileImage() {
        val profileImage = carryOnApplication.loginCustomerModel.userImage

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
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$fileName")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            showImage2State.value = true
            imageUriState.value = uri
        }.addOnFailureListener { e ->
            println("Firebase Storage 이미지 로드 실패: ${e.localizedMessage}")
            showImage1State.value = true // 기본 이미지 표시
        }
    }

    // 다이얼로그 바로가기 버튼을 눌렀을때
    fun dialogConfirmOnClick() {
        carryOnApplication.navHostController.popBackStack(ScreenName.EDIT_MY_INFO.name, inclusive = true)
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name,)
    }

    // 다이얼로그 취소 버튼을 눌렀을때
    fun dialogDismissOnClick() {
        showDialogBackArrowState.value = false
    }

    // 비밀번호 변경을 눌렀을때
    fun modifyPwOnClick() {
        carryOnApplication.navHostController.navigate(ScreenName.EDIT_PW.name,)
    }

    // 회원 탈퇴를 눌렀을때
    fun withdrawalOnClick() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                //customerService.updateUserState(shoppingApplication.loginCustomerModel.customerDocumentId, UserState.USER_STATE_SIGNOUT)
            }
            work1.join()

            // carryOnApplication.isLoggedIn.value = false
            carryOnApplication.navHostController.popBackStack(ScreenName.EDIT_MY_INFO.name, inclusive = true)
            carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name)
        }
    }

    // 저장하기 버튼
    fun saveSettingButtonOnClick() {

        CoroutineScope(Dispatchers.Main).launch {
            // 첨부 이미지가 있다면
            if(imageUriState.value != null){
                // 만약 이미지를 삭제했다면
                if(showImage1State.value){
                    // 이미지 파일을 삭제한다.
                    val work1 = async(Dispatchers.IO) {
                       // customerService.removeImageFile(shoppingApplication.loginCustomerModel.customerUserNickName)
                    }
                    work1.join()
                    newFileName = "none"
                }
            }

            // 카메라나 앨범에서 사진을 가져온 적이 있다면
            if(showImage3State.value){
                // 첨부 이미지가 있다면
                if(imageUriState.value != null){
                    // 이미지 파일을 삭제한다.
                    val work1 = async(Dispatchers.IO) {
                       // customerService.removeImageFile(shoppingApplication.loginCustomerModel.customerUserNickName)
                    }
                    work1.join()
                }

                // 서버상에서의 파일 이름
                newFileName = "image_${System.currentTimeMillis()}.jpg"
                // 로컬에 ImageView에 있는 이미지 데이터를 저장한다.
                Tools.saveBitmap(carryOnApplication,  imageBitmapState.value!!)

                val work2 = async(Dispatchers.IO){
                    val filePath = carryOnApplication.getExternalFilesDir(null).toString()
                    //customerService.uploadImage("${filePath}/uploadTemp.jpg", newFileName)
                }
                work2.join()
            }

            carryOnApplication.loginCustomerModel.userImage = newFileName
            carryOnApplication.loginCustomerModel.userName = textFieldModifyNameValue.value
            //carryOnApplication.loginCustomerModel.userPhoneNumber = textFieldModifyPhoneValue.value
            //carryOnApplication.loginCustomerModel.userId = textFieldModifyIdValue.value
            carryOnApplication.loginCustomerModel.userAppPushAgree = selectedPushAgree.value

            val work3 = async(Dispatchers.IO) {
                // 저장 로직
               // customerService.updateUserData(shoppingApplication.loginCustomerModel)
            }
            work3.join()

            Toast.makeText(carryOnApplication, "수정이 완료되었습니다", Toast.LENGTH_SHORT).show()
            carryOnApplication.navHostController.popBackStack(ScreenName.EDIT_MY_INFO.name, inclusive = true)
            carryOnApplication.navHostController.navigate(ScreenName.MY_PAGE.name)
        }
    }

    fun navigationIconOnClick() {

        // 뒤로가기
        carryOnApplication.navHostController.popBackStack(ScreenName.EDIT_MY_INFO.name, inclusive = true)
        carryOnApplication.navHostController.navigate(ScreenName.MY_PAGE.name)

    }

    // 이미지 삭제 버튼을 눌렀을 때
    fun deleteImageOnClick(){
        showImage1State.value = false
        showImage2State.value = false
        showImage3State.value = false

        // 카메라나 앨범에서 가져온 사진이 있다면 삭제한다.
        imageBitmapState.value = null

        // 기본 이미지를 보여준다.
        showImage1State.value = true
    }


    // 단일 이미지 추가
    fun addSingleImage(uri: Uri) {
        if (_imageUris.value.size < 10) {
            _imageUris.value = _imageUris.value + uri
        }
    }

    // 이미지 삭제
    fun removeImage(index: Int) {
        _imageUris.value = _imageUris.value.toMutableList().apply { removeAt(index) }
    }


}