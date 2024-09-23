package com.blackcows.butakaeyak.ui.schedule.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ScheduleProfileItemBinding
import com.bumptech.glide.Glide

class ProfileRvAdapter(
    private val onProfileClick: (String) -> Unit
): ListAdapter<ScheduleProfile, ProfileRvAdapter.ProfileViewHolder>(
    object: DiffUtil.ItemCallback<ScheduleProfile>() {
        override fun areItemsTheSame(oldItem: ScheduleProfile, newItem: ScheduleProfile): Boolean {
            return oldItem.userId == newItem.userId
        }
        override fun areContentsTheSame(oldItem: ScheduleProfile, newItem: ScheduleProfile): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class ProfileViewHolder(private val binding: ScheduleProfileItemBinding): ViewHolder(binding.root) {
        fun bind(item: ScheduleProfile) = with(binding) {
            Glide.with(itemView).load(item.profileUrl).into(profileIv)
            nameTv.text = item.name

            binding.root.setOnClickListener {
                onProfileClick(item.userId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_profile_item, parent, false)
        return ProfileViewHolder(ScheduleProfileItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}