package com.blackcows.butakaeyak.ui.take

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.TakeAddMedicine
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.ui.take.data.CycleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakeAddViewModel @Inject constructor(
    private val medicineGroupRepository: MedicineGroupRepository
) : ViewModel() {
    private val _medicineGroup = MutableLiveData<MedicineGroup?>(null)
    val medicineGroup get() = _medicineGroup
    private val medicineNameList = mutableListOf<String>()
    private var _nameRvGroup = MutableLiveData<List<TakeAddMedicine>>(null)
    val nameRvGroup get() = _nameRvGroup

    fun saveGroup () {
        viewModelScope.launch {
            medicineGroup.value?.let {
                medicineGroupRepository.saveNewGroup(it)
            }
        }
    }

    fun saveNames(medicines: MutableList<String>){
        medicineNameList.addAll(medicines)
    }
    fun loadNames() {
        _nameRvGroup.value = medicineNameList.map {
            TakeAddMedicine(
                imageUrl = null,
                name = it,
                isDetail = false
            )
        }
    }
    fun addNames(imageUrl: String, name: String) {
        _nameRvGroup.value = _nameRvGroup.value?.toMutableList().apply {
            this?.add(TakeAddMedicine(imageUrl, name, false))
        }
    }

}