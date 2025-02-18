package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

data class PlaceList(
    val imageRes: Int,
    val title: String,
    val region: String,
    val category: String,
    val address: String,
    val call: String,
    val content: String,
)

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel()  {

    val carryOnApplication = context as CarryOnApplication

    // 검색을 위한 임시 데이터
    val _places = MutableStateFlow(
        listOf(
            PlaceList(
                imageRes = R.drawable.sample1,
                title = "라마다프라자호텔",
                region = "전라남도",
                category = "숙박",
                address = "전라남도 신안군 자은면 자은서부1길 163-101",
                call = "061-988-8888",
                content = "22년 6월 오픈 신상 숙소이다. 호텔&리조트 도보 100m 앞 ‘백길해수욕장’ 위치해 있으며, 사계절 미온수풀 실내 워터파크가 준비되어 있다. 사우나, 워터파크, 갤러리카페, 키즈카페, 스카이라운지, 편의점, 오락실, 코인세탁실, 캐쥬얼레스토랑, 펍(PUB) 등 다채로운 부대시설을 즐길 수 있다. 연계 관광지로 UN 최우수 관광마을 ‘퍼플섬’, 마리나 요트관광, 아름다운 해송숲이 어우러진 수석미술관,수설정원, 조개박물관이 있는 ‘1004 뮤지엄’ 방문으 추천한다."
            ),
            PlaceList(
                imageRes = R.drawable.sample2,
                title = "남산서울타워",
                region = "서울",
                category = "관광지",
                address = "서울특별시 용산구 남산공원길 105 ",
                call = "02-3455-9277",
                content = "남산서울타워는 효율적인 방송전파 송수신과 한국의 전통미를 살린 관광 전망시설의 기능을 겸비한 국내 최초의 종합전파탑으로 방송문화와 관광산업의 미래를 위해 건립되었다. 세계 유명한 종합 탑들이 그 나라 또는 그 도시의 상징적인 존재가 된 것처럼 ‘남산서울타워’ 역시 지난 40여 년간 대한민국의 대표적인 관광지이자 서울의 상징물 역할을 해왔다. 남산서울타워는 서울 시내 전 지역에서 바라보이는 탑의 높이와 독특한 구조, 형태 등으로 인하여 시민의 관심과 사랑의 대상이 되었고, 내외국인들이 즐겨 찾는 제1의 관광 명소로서의 위치를 확고히 하고 있다. 최근에는 한류 바람을 몰고 온 각종 예능, 드라마의 촬영지로 이름이 높아지면서 내외국인 관광객들이 발길이 끊이지 않는 곳이다. ◎ 한류의 매력을 만나는 여행 정보 TXT 자체 콘텐츠 에서 막내 휴닝카이와 태현이 교복 나들이를 즐긴 장소다. 두 사람은 케이블카를 타고 정상에 올라 함께 사진을 찍고, 소원 자물쇠를 채우며 시간을 보냈다. 자물쇠는 케이블카 상류 정류장에 위치한 기념품점에서 구매할 수 있고, 소원을 적을 때 쓰는 네임펜도 대여 가능하다."
            )

        )
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
        val filterSearchList = _places.value.filter { _place ->
            keywords.any { keyword ->
                _place.title.contains(keyword, ignoreCase = true) ||
                        _place.region.contains(keyword, ignoreCase = true) ||
                        _place.category.contains(keyword, ignoreCase = true) ||
                        _place.address.contains(keyword, ignoreCase = true) ||
                        _place.content.contains(keyword, ignoreCase = true)
            }
        }

        placeSearchList.clear()
        placeSearchList.clear()
        placeSearchList.addAll(filterSearchList.map { _place ->
            val placeMap = mapOf(
                "imageRes" to (_place.imageRes ?: R.drawable.hide_image_24px), // 기본 이미지 설정
                "title" to _place.title,
                "region" to _place.region,
                "category" to _place.category,
                "address" to _place.address,
                "call" to _place.call,
                "content" to _place.content
            )
            placeMap
        })

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