package com.blackcows.butakaeyak.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.KakaoPlace
import com.blackcows.butakaeyak.domain.repo.KakaoMapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "k3f_MapViewModel"

@HiltViewModel
class MapViewModel @Inject constructor(
    private val kakaoRepository: KakaoMapRepository
) : ViewModel() {
    private val _items = MutableLiveData<List<KakaoPlace>>()
    val items: LiveData<List<KakaoPlace>>
        get() = _items

    fun communicateNetWork(x: Double, y: Double) {
        viewModelScope.launch {
            _items.value = kakaoRepository.searchCategory(x.toString(), y.toString())
            Log.d(TAG, "${_items.value}")
        }
    }
}