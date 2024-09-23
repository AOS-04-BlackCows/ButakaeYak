package com.blackcows.butakaeyak.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.MyPharmacy
import com.blackcows.butakaeyak.domain.repo.MyPharmacyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPharmacyViewModel @Inject constructor(
    private val myPharmacyRepository: MyPharmacyRepository
): ViewModel() {
    private val _myPharmacies = MutableLiveData<Set<MyPharmacy>>(setOf())
    val myPharmacies get() = _myPharmacies

    fun getPharmacyList(userId: String){
        viewModelScope.launch {
            val myList = myPharmacyRepository.getMyFavorites(userId)
            _myPharmacies.value = myList.toSet()
        }
    }

    fun addToFavoritePharmacyList(userId: String, pharmacy: MyPharmacy){
        viewModelScope.launch {
            myPharmacyRepository.addToFavorite(pharmacy)
            getPharmacyList(userId)
        }
    }

    fun cancelFavoritePharmacy(userId: String, pharmacyId: String) {
        if(myPharmacies.value!!.isNotEmpty()) {
            throw Exception("Please call getPharmacyList() to initialize LiveData.")
        }

        viewModelScope.launch {
            myPharmacies.value!!.firstOrNull {
                it.pharmacyId != pharmacyId
            }?.let {
                myPharmacyRepository.cancelFavorite(userId, it)
            }
        }
    }

    fun isPharmacyChecked(id: String) : Boolean{
        return myPharmacies.value!!.any {
            it.pharmacyId == id
        }
    }
}