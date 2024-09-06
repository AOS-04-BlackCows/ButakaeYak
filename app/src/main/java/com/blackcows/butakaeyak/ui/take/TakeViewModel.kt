package com.blackcows.butakaeyak.ui.take

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.ui.take.data.CycleItem

class TakeViewModel : ViewModel() {

    //editText
    private val nameFragmentEditText : MutableLiveData<String> = MutableLiveData()

    //recyclerView textView
    private val formFragmentText : MutableLiveData<String?> = MutableLiveData()

    //recyclerView textView
    private val formImageFragmentText : MutableLiveData<Int> = MutableLiveData()

    //take from cycle
    private val cycleFragment = MutableLiveData<MutableList<CycleItem>?>()
    val _cycleFragment : LiveData<MutableList<CycleItem>?> get() = cycleFragment

    init {
        cycleFragment.value = mutableListOf()
    }

    fun updateCycleData(cycleItem: CycleItem) {
        val cycleList = cycleFragment.value ?: mutableListOf()
        cycleList.add(cycleItem)
        cycleFragment.value = cycleList
    }

    fun removeCycleItem(position: Int) {
        val updatedList = cycleFragment.value
        if (updatedList != null && position >= 0 && position <= updatedList.size) {
            updatedList.removeAt(position)
            cycleFragment.value = updatedList
        }
    }

    //viewPager2
    private val _currentPage = MutableLiveData<Int>()
    val currentPage : LiveData<Int> get() = _currentPage

    //timepicker dialog
    private val _selectedTimes = MutableLiveData<MutableList<Pair<Int, Int>>>().apply { value = mutableListOf() }
    val selectedTimes: LiveData<MutableList<Pair<Int, Int>>> get() = _selectedTimes

    //timepicker dialog 데이터 추가하기
    fun addTime(hour: Int, minute: Int) {
        val currentList = _selectedTimes.value ?: mutableListOf()
        currentList.add(Pair(hour, minute))
        _selectedTimes.value = currentList
    }

    //timepicker dialog 데이터 제거하기
    fun removeTime(position: Int) {
        val currentList = _selectedTimes.value ?: mutableListOf()
        if (position >= 0 && position < currentList.size) {
            currentList.removeAt(position)
            _selectedTimes.value = currentList
        }
    }

    //editText 데이터 받기
    fun getData(): MutableLiveData<String> = nameFragmentEditText

    //recyclerView Text 내용 넘기기
    fun getTextData(): MutableLiveData<String?> = formFragmentText

    //recyclerView Text 내용 넘기기
    fun getImageData(): MutableLiveData<Int> = formImageFragmentText

    //editText 내용 넘기기
    fun updateItem(text :String) {
        nameFragmentEditText.value = text
    }

    //recyclerView textView 내용 넘기기
    fun updateFormItem(text:String?){
        formFragmentText.value = text
    }

    //recyclerView textView 내용 넘기기
    fun updateFormImageItem(image:Int){
        formImageFragmentText.value = image
    }

}