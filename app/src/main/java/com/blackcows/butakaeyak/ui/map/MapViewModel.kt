package com.blackcows.butakaeyak.ui.map

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.domain.repo.KakaoMapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "k3f_MapViewModel"

@HiltViewModel
class MapViewModel @Inject constructor(
    private val kakaoRepository: KakaoMapRepository
) : ViewModel() {
    private val _items = MutableLiveData<List<KakaoPlacePharmacy>>(listOf())
    val items: LiveData<List<KakaoPlacePharmacy>>
        get() = _items

    var pharmacyPager = 0

    fun findPharmacy(x: Double, y: Double) {
        pharmacyPager = 1
        viewModelScope.launch {
            _items.value = listOf()
            _items.value = kakaoRepository.searchCategory(x.toString(), y.toString(), pharmacyPager)
            Log.d(TAG, "${_items.value}")
        }
    }
    fun moreFindPharmacy(x: Double, y: Double) {
        pharmacyPager++
        viewModelScope.launch {
            val morePharmacy = kakaoRepository.searchCategory(x.toString(), y.toString(), pharmacyPager)
            val pharmacyList = _items.value?: listOf<KakaoPlacePharmacy>()
            val updatePharmacy = pharmacyList.toMutableList()
            updatePharmacy.addAll(morePharmacy)
            _items.value = updatePharmacy
            Log.d(TAG, "${_items.value}")
        }
    }

}