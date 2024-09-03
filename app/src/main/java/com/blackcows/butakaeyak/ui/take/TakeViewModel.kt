package com.blackcows.butakaeyak.ui.take

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TakeViewModel : ViewModel() {

    private val nameFragmentEditText : MutableLiveData<String> = MutableLiveData()

    fun getData(): MutableLiveData<String> = nameFragmentEditText

    fun updateItem(text :String) {
        nameFragmentEditText.value = text
    }

}