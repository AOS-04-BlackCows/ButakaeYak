package com.blackcows.butakaeyak.ui.take.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.MyMedicineItemBinding
import com.blackcows.butakaeyak.databinding.TodayMedicineItemBinding
import com.blackcows.butakaeyak.ui.GlideApp
import com.blackcows.butakaeyak.ui.home.MedicineResultFragment
import com.blackcows.butakaeyak.ui.home.SearchDetailFragment
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.blackcows.butakaeyak.ui.take.fragment.CycleFragment
import com.bumptech.glide.Glide

class MyMedicinesRvAdapter:
    ListAdapter<MyMedicine, MyMedicinesRvAdapter.MedicineViewHolder>(
        object: DiffUtil.ItemCallback<MyMedicine>() {
            override fun areItemsTheSame(oldItem: MyMedicine, newItem: MyMedicine): Boolean {
                //Log.d("DiffUtil", "old: ${oldItem.imageUrl}, new: ${newItem.imageUrl}}")
                return  (oldItem.medicine.id == newItem.medicine.id)
            }
            override fun areContentsTheSame(oldItem: MyMedicine, newItem: MyMedicine): Boolean {
                //Log.d("DiffUtil", "old is same with new: ${oldItem == newItem}")
                return oldItem == newItem
            }
        }
    ) {

        inner class MedicineViewHolder(private val binding: MyMedicineItemBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(item: MyMedicine) {
                val imageUrl = item.medicine.imageUrl
                if(imageUrl?.isNotEmpty() == true) {
                    Glide.with(itemView).load(imageUrl).into(binding.medicineIv)
                }

                with(binding) {
                    medicineNameTv.text = item.medicine.name
                    medicineEffectTv.text = item.medicine.effect

                    root.setOnClickListener {
                        MainNavigation.addFragment(
                            SearchDetailFragment.newInstance(item.medicine), FragmentTag.SearchDetailFragmentInTake
                        )
                    }
                    tvMedicineAlarmIv.setOnClickListener {
                        //TODO: navigate to MyMedicineDetailPage.
                        MainNavigation.addFragment(
                            CycleFragment.newInstance(item.medicine), FragmentTag.CycleFragment
                        )
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_medicine_item, parent, false)
        return MedicineViewHolder(MyMedicineItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}