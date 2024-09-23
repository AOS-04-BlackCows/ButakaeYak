package com.blackcows.butakaeyak.ui.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Memo
import com.blackcows.butakaeyak.domain.repo.MemoRepository
import com.blackcows.butakaeyak.ui.note.recycler.RecyclerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val memoRepository: MemoRepository
): ViewModel() {
    private val _memos = MutableLiveData<List<Memo>>(listOf())
    val memos  get() = _memos

    fun initMemos(userId: String) {
        viewModelScope.launch {
            _memos.value = memoRepository.getMemosByUserId(userId)
        }
    }

    fun getDateMemos(): List<RecyclerItem.DateMemos> {
        return memos.value!!.groupBy {
            it.createdAt
        }.map {
            RecyclerItem.DateMemos(
                it.key, it.value
            )
        }
    }

    fun getGroupMemos(): List<RecyclerItem.GroupMemos> {
        return memos.value!!.groupBy {
            it.group
        }.map {
            RecyclerItem.GroupMemos(
                it.key, it.value
            )
        }
    }
}