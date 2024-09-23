package com.blackcows.butakaeyak.ui.schedule.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.ScheduleItemBinding
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.TimeToGroup

class ScheduleRvAdapter(
    private val isDisabled: Boolean,
    private val clickListener: ClickListener,
): ListAdapter<TimeToGroup, ScheduleRvAdapter.TimeToGroupViewHolder>(
    object: DiffUtil.ItemCallback<TimeToGroup>() {
        override fun areItemsTheSame(oldItem: TimeToGroup, newItem: TimeToGroup): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: TimeToGroup, newItem: TimeToGroup): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class TimeToGroupViewHolder(private val binding: ScheduleItemBinding): ViewHolder(binding.root) {
        fun bind(item: TimeToGroup) {
            val onTakingCheck: (MedicineGroup, Boolean, String) -> Unit = { group, isTaken, alarm ->
                clickListener.onCheckClick(group, isTaken, alarm)
            }
            val onModify: (MedicineGroup) -> Unit = { group ->
                clickListener.onModifyClick(group)
            }
            val timeGroupRvAdapter = TimeGroupRvAdapter(item.alarm, isDisabled, onModify, onTakingCheck)
            with(binding) {
                timeTv.text = item.alarm

                groupRv.run {
                    adapter = timeGroupRvAdapter
                    layoutManager = LinearLayoutManager(binding.root.context)
                    NoteRvDecoration.getLinearDecoSimpleItem()
                }

                timeGroupRvAdapter.submitList(item.groups)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeToGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return TimeToGroupViewHolder(ScheduleItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: TimeToGroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface ClickListener {
        fun onModifyClick(medicineGroup: MedicineGroup)
        fun onCheckClick(medicineGroup: MedicineGroup, taken: Boolean, alarm: String)
    }
}