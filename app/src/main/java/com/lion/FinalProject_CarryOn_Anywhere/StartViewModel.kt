//package com.lion.FinalProject_CarryOn_Anywhere
//
//import android.content.Context
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
//import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
//import dagger.hilt.android.lifecycle.HiltViewModel
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.async
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class StartViewModel @Inject constructor(
//    @ApplicationContext val context: Context,
//) : ViewModel() {
//
//    // 로그인 중입니다를 위한 상태 관리 변수
//    val showLoginMessageState = mutableStateOf(false)
//
//    val carryOnApplication = context as CarryOnApplication
//
//    // 자동 로그인 처리
//    fun autoLoginProcess(){
//        // Preference에 login token이 있는지 확인한다.
//        val pref = carryOnApplication.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
//        val loginToken = pref.getString("token", null)
//        // Log.d("test100", "$loginToken")
//
//        CoroutineScope(Dispatchers.Main).launch {
//            if(loginToken != null){
//
//                showLoginMessageState.value = true
//
//                // 사용자 정보를 가져온다.
//                val work1 = async(Dispatchers.IO){
//                    UserService.selectUserDataByLoginToken(loginToken)
//                }
//                val loginUserModel = work1.await()
//                // 가져온 사용자 데이터가 있다면
//                if(loginUserModel != null){
//
//                    carryOnApplication.loginUserModel = loginUserModel
//
//                    carryOnApplication.navHostController.popBackStack(ScreenName.START_SCREEN.name, true)
//                    carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
//                } else {
//                    // 로그인 화면으로 이동
//                    carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name)
//                }
//            } else {
//                // 로그인 화면으로 이동한다.
//                carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name)
//            }
//        }
//    }
//}