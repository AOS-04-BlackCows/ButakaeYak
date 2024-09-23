package com.blackcows.butakaeyak.ui.note.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Memo
import com.blackcows.butakaeyak.databinding.NoteItemBinding

class NoteItemRvAdapter(

): ListAdapter<RecyclerItem, RecyclerView.ViewHolder>(
    object: DiffUtil.ItemCallback<RecyclerItem>() {
        override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            return  (oldItem == newItem)
        }
        override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            return oldItem == newItem
        }
    }
) {

    companion object {
        private const val TYPE_DATE = 1
        private const val TYPE_GROUP = 2
    }


    inner class DateMemosViewHolder(private val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecyclerItem.DateMemos) {
            with(binding) {
                titleTv.text = item.date.toString().replace("-", ".")
                dateTv.visibility = View.INVISIBLE
                noteNumTv.text = item.memos.size.toString()
            }
        }
    }

    inner class GroupMemoViewHolder(private val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecyclerItem.GroupMemos) {
            with(binding){
                titleTv.text = item.group.name
                dateTv.text = item.group.startedAt.toString().replace("-", ".")
                noteNumTv.text = item.memos.size.toString()
            }
        }
    }



    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecyclerItem.DateMemos -> TYPE_DATE
            is RecyclerItem.GroupMemos -> TYPE_GROUP
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_DATE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
                DateMemosViewHolder(NoteItemBinding.bind(view))
            }
            TYPE_GROUP -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
                GroupMemoViewHolder(NoteItemBinding.bind(view))
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is DateMemosViewHolder -> holder.bind(getItem(position) as RecyclerItem.DateMemos)
            is GroupMemoViewHolder -> holder.bind(getItem(position) as RecyclerItem.GroupMemos)
        }
    }
}