package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class WriteRequestPlaceViewModel @Inject constructor(
    @ApplicationContext context: Context
): ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    val textFieldPlaceName = mutableStateOf("")

    val textFieldAddress = mutableStateOf("")

    val showAddressSearch = mutableStateOf(false)

    val requestPlaceDialogState = mutableStateOf(false)
}