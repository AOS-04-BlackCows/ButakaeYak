package com.blackcows.butakaeyak.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.MypageItemFavoriteBinding

class UserFavoriteViewPagerAdapter(val items : MutableList<test>) : RecyclerView.Adapter<UserFavoriteViewPagerAdapter.ViewPagerHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserFavoriteViewPagerAdapter.ViewPagerHolder {
        val binding = MypageItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewPagerHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserFavoriteViewPagerAdapter.ViewPagerHolder,
        position: Int
    ) {
        holder.aImage.setImageResource(R.drawable.choco)
        holder.aName.text = "항히스타민제"
        holder.aGroup.text = "피부 질환 완화제"

    }

    override fun getItemCount(): Int {
         return items.size
    }

    inner class ViewPagerHolder(private val binding : MypageItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root){
        val aImage = binding.ivMedicine
        val aName = binding.tvMedicineNameInfo
        val aGroup = binding.tvMedicineGroupInfo
    }
}