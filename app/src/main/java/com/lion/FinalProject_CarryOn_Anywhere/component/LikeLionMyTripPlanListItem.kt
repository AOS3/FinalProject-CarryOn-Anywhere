package com.lion.FinalProject_CarryOn_Anywhere.component


import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ✅ 개별 댓글 아이템
@Composable
fun LikeLionMyTripPlanItem(
    plan: TripModel,
    onDeleteClick: () -> Unit,
    onclick: () -> Unit = { }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onclick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // ✅ 텍스트가 남은 공간을 차지하도록 설정
                .padding(end = 10.dp)
                .padding(bottom = 10.dp)

        ) {
            // ✅ 제목
            Text(
                text = plan.tripTitle,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(4.dp))

            val startDate = plan.tripStartDate.toFormattedDateString() // Long → String 변환
            val endDate = plan.tripEndDate.toFormattedDateString()

            // ✅ 날짜
            Text(
                text = if (endDate == "" || startDate == endDate) {
                    "$startDate ~ $startDate"
                }else {
                    "$startDate ~ $endDate"
                 },
                fontSize = 15.sp,
                color = Color.Gray
            )
        }

        // ✅ 삭제 아이콘
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Comment",
            modifier = Modifier
                .size(25.dp)
                .clickable { onDeleteClick() },
            tint = Color.Gray
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun LikeLionMyTripPlanList(
    planList: List<TripModel>,
    onDeleteConfirmed: (TripModel) -> Unit
) {
    // ✅ 다이얼로그 상태 및 삭제할 댓글 상태
    val showDialog = remember { mutableStateOf(false) }
    val selectedPlan = remember { mutableStateOf<TripModel?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(planList) { plan ->
            LikeLionMyTripPlanItem(
                plan = plan,
                onDeleteClick = {
                    selectedPlan.value = plan
                    showDialog.value = true // ✅ 삭제 다이얼로그 표시
                }
            )
        }
    }

    // ✅ 삭제 확인 다이얼로그
    if (showDialog.value && selectedPlan.value != null) {
        LikeLionAlertDialog(
            showDialogState = showDialog,
            title = "댓글을 삭제하시겠어요?",
            text = "데이터가 모두 삭제되며 복구할 수 없습니다.",
            confirmButtonTitle = "삭제",
            confirmButtonOnClick = {
                selectedPlan.value?.let { onDeleteConfirmed(it) }
                showDialog.value = false
            },
            dismissButtonTitle = "취소",
            dismissButtonOnClick = {
                showDialog.value = false
            }
        )
    }
}

// ✅ 미리보기
@Preview(showBackground = true)
@Composable
fun PreviewLikeLionMyTripPlanList() {
    val testPlans = listOf(
        TripModel()
    )

    LikeLionMyTripPlanList(
        planList = testPlans,
        onDeleteConfirmed = { deletedPlan ->
            // ✅ 삭제 처리 로직 (현재는 테스트용 출력)
           println("Deleted: ${deletedPlan.tripTitle}")
        }
    )
}

fun Long.toFormattedDateString(): String {
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA) // 원하는 날짜 형식
    return dateFormat.format(Date(this)) // Long 값을 Date로 변환 후 포맷 적용
}