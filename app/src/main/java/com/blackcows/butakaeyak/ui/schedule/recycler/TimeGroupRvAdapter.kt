package com.blackcows.butakaeyak.ui.schedule.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.TodayGroupItemBinding
import java.time.LocalDate

class TimeGroupRvAdapter(
    private val alarm: String,
    private val isDisabled: Boolean,
    private val onModifyClick: (MedicineGroup) -> Unit,
    private val onCheckClick: (MedicineGroup, Boolean, String) -> Unit
): ListAdapter<MedicineGroup, TimeGroupRvAdapter.MedicineGroupViewHolder>(
    object: DiffUtil.ItemCallback<MedicineGroup>() {
        override fun areItemsTheSame(oldItem: MedicineGroup, newItem: MedicineGroup): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: MedicineGroup, newItem: MedicineGroup): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class MedicineGroupViewHolder(private val binding: TodayGroupItemBinding): ViewHolder(binding.root) {
        fun bind(item: MedicineGroup) {
            with(binding) {
                groupNameTv.text = item.name
                groupNameTv.text = "약 ${item.medicines.size}개"

                if(isDisabled) {
                    takenCb.isEnabled = false
                    modifyBtn.visibility = View.GONE
                }

                val timeToTaken = "${LocalDate.now()} $alarm"
                if(item.hasTaken.contains(timeToTaken)){
                    takenCb.isSelected = true
                }

                takenCb.setOnClickListener {
                    takenCb.isSelected = !takenCb.isSelected
                    onCheckClick(item, takenCb.isSelected, alarm)
                }

                modifyBtn.setOnClickListener {
                    if(isDisabled) return@setOnClickListener
                    onModifyClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.today_group_item, parent, false)
        return MedicineGroupViewHolder(TodayGroupItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MedicineGroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}