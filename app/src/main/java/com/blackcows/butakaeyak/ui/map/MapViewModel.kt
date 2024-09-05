package com.blackcows.butakaeyak.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.repository.KakaoMapRepository
import com.blackcows.butakaeyak.data.retrofit.ApiBaseUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "k3f_MapViewModel"

@HiltViewModel
class MapViewModel @Inject constructor(
    private val kakaoRepository: KakaoMapRepository
) : ViewModel() {
    private val _items = MutableLiveData<List<KakaoPlacePharmacy>>()
    val items: LiveData<List<KakaoPlacePharmacy>>
        get() = _items

    fun communicateNetWork(x: Double, y: Double) {
        viewModelScope.launch {
            _items.value = kakaoRepository.searchCategory(x.toString(), y.toString())
            Log.d(TAG, "${_items.value}")
        }
    }
}