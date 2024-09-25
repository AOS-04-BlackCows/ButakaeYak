package com.blackcows.butakaeyak.ui.home.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.HomeRvGroup
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.databinding.ItemHomeTodayMedicineGroupBinding
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewPharmacyListBinding

class HomeTodayMedicineRvAdapter(private val clickListener: ClickListener): ListAdapter<HomeRvGroup, HomeTodayMedicineRvAdapter.ToddayMedicineListViewHolder>(
        object: DiffUtil.ItemCallback<HomeRvGroup>() {
            override fun areItemsTheSame(oldItem: HomeRvGroup, newItem: HomeRvGroup): Boolean {
                //Log.d("DiffUtil", "old: ${oldItem.imageUrl}, new: ${newItem.imageUrl}}")
                return  (oldItem.groupId == newItem.groupId && oldItem.alarmTime == newItem.alarmTime)
            }
            override fun areContentsTheSame(oldItem: HomeRvGroup, newItem: HomeRvGroup): Boolean {
                //Log.d("DiffUtil", "old is same with new: ${oldItem == newItem}")
                return oldItem == newItem
            }

        }
) {
    inner class ToddayMedicineListViewHolder(private val binding: ItemHomeTodayMedicineGroupBinding): ViewHolder(binding.root) {
        fun bind(item: HomeRvGroup, position: Int) {
            with(binding) {
                tvHomeTodayMedicineTime.text = item.alarmTime
                tvHomeTodayMedicineGroupName.text = item.groupName

                btnHomeMedicineCheck.isSelected = item.isHasTakenTime
                if (btnHomeMedicineCheck.isSelected) {
                    // 먹었음
                    btnHomeMedicineCheck.text = "먹음"
                    btnHomeMedicineCheck.setTextColor(Color.WHITE)
                } else {
                    // 안먹음
                    btnHomeMedicineCheck.text = "안먹음"
                    btnHomeMedicineCheck.setTextColor(binding.root.context.resources.getColor(R.color.black))
                }

                layoutTodayMedicine.setOnClickListener {
                    clickListener.onTodayMedicineClick(item, position)
                }
                btnHomeMedicineCheck.setOnClickListener {
                    btnHomeMedicineCheck.isSelected = !btnHomeMedicineCheck.isSelected
                    if (btnHomeMedicineCheck.isSelected) {
                        // 먹었음
                        btnHomeMedicineCheck.text = "먹음"
                        btnHomeMedicineCheck.setTextColor(Color.WHITE)
                    } else {
                        // 안먹음
                        btnHomeMedicineCheck.text = "안먹음"
                        btnHomeMedicineCheck.setTextColor(binding.root.context.resources.getColor(R.color.black))
                    }

                    clickListener.onAlarmClick(item, position, btnHomeMedicineCheck.isSelected)
                }
            }
        }
    }

    var itemClick : ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToddayMedicineListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_today_medicine_group, parent, false)
        return ToddayMedicineListViewHolder(ItemHomeTodayMedicineGroupBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ToddayMedicineListViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    interface ClickListener {
        fun onTodayMedicineClick(item: HomeRvGroup, position: Int)
        fun onAlarmClick(item: HomeRvGroup, position: Int, isSelected: Boolean)
    }

}