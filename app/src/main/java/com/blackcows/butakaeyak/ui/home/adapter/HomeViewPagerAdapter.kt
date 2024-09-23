package com.blackcows.butakaeyak.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.ItemHomeTodayMedicineBinding

class HomeViewPagerAdapter(
    private val onClickItem: (MedicineGroup) -> Unit
) : ListAdapter<MedicineGroup, HomeViewPagerAdapter.ViewPagerHolder>(
    object : DiffUtil.ItemCallback<MedicineGroup>() {
        override fun areItemsTheSame(oldItem: MedicineGroup, newItem: MedicineGroup): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: MedicineGroup, newItem: MedicineGroup): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class ViewPagerHolder(private val binding : ItemHomeTodayMedicineBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: MedicineGroup) {
            with(binding) {
                ivTodayMedicine.setImageResource(R.drawable.medicine_type_1)
                tvTodayMedicineTime.text = item.alarms[0]
                tvTodayMedicineName.text = item.name
                layoutTodayMedicine.setOnClickListener {
                    onClickItem(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewPagerAdapter.ViewPagerHolder {
        val binding = ItemHomeTodayMedicineBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewPagerHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeViewPagerAdapter.ViewPagerHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


}