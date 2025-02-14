package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel()  {

    val carryOnApplication = context as CarryOnApplication

    // 검색을 위한 임시 데이터
    private val allPlaces = listOf(
        mapOf("name" to "라마다프라자호텔 자은도", "region" to "전라남도", "category" to "숙박"),
        mapOf("name" to "서울 타워", "region" to "서울", "category" to "관광지"),
        mapOf("name" to "부산 해운대", "region" to "부산", "category" to "관광지"),
        mapOf("name" to "제주 성산일출봉", "region" to "제주도", "category" to "자연경관"),
        mapOf("name" to "경주 불국사", "region" to "경상북도", "category" to "문화유산"),
    )

    // 검색어
    var searchValue = mutableStateOf("")
    // 검색 결과 리스트
    var placeSearchList = mutableStateListOf<Map<String, Any>>()
    // 찜 상태 관리
    var isFavoriteEnable = mutableStateOf(false)
    // 검색 버튼 누름 여부
    var isSearchTriggered = mutableStateOf(false)


    // Back 버튼 동작 메서드
    fun navigationBackIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.PLACE_SEARCH_SCREEN.name,
            inclusive = true
        )
        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name) {
            launchSingleTop = true
        }
    }

    // 검색 결과 리스트 메서드
    fun performSearch() {

        // TODO : 검색 결과 출력
        // 검색어 분리해서 가져오기
        val searchInput = searchValue.value.trim()

        // 검색어가 비어있다면 전체 리스트를 없앤다
        if (searchInput.isEmpty()) {
            placeSearchList.clear()
            return
        }

        // 입력된 검색어를 공백 기준으로 여러 개의 키워드로 분리한다.
        val keywords = searchInput.split(" ").map { it.trim() }.filter { it.isNotEmpty() }

        // 키워드 중 하나라도 포함된 장소를 찾는다.
        val filterSearchList = allPlaces.filter { place ->
            keywords.any { keyword ->
                place["name"].toString().contains(keyword, ignoreCase = true) ||
                        place["region"].toString().contains(keyword, ignoreCase = true) ||
                        place["category"].toString().contains(keyword, ignoreCase = true)
            }
        }

        placeSearchList.clear()
        placeSearchList.addAll(filterSearchList)

    }

    // 검색 버튼 동작 메서드
    fun searchAndHideKeyboard(keyboardController: SoftwareKeyboardController?) {
        performSearch()
        keyboardController?.hide()
    }

    // 검색 필드 초기화 메서드
    fun clearSearch() {
        searchValue.value = ""
        placeSearchList.clear()
        isSearchTriggered.value = false
    }



    // 찜 아이콘 상태 변경
    fun toggleFavorite() {
        isFavoriteEnable.value = !isFavoriteEnable.value
    }


}