package com.blackcows.butakaeyak.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.databinding.MypageItemFavoriteBinding

class UserFavoriteViewPagerAdapter(
    private val onClickRemove: (KakaoPlacePharmacy) -> Unit
) : ListAdapter<KakaoPlacePharmacy, UserFavoriteViewPagerAdapter.ViewPagerHolder>(
    object : DiffUtil.ItemCallback<KakaoPlacePharmacy>() {
        override fun areItemsTheSame(oldItem: KakaoPlacePharmacy, newItem: KakaoPlacePharmacy): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: KakaoPlacePharmacy, newItem: KakaoPlacePharmacy): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {

    inner class ViewPagerHolder(private val binding : MypageItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: KakaoPlacePharmacy) {
            with(binding) {
                ivMedicine.setImageResource(R.drawable.pharmacy)
                tvMedicineNameInfo.text = item.placeName
                tvMedicineGroupInfo.text = item.phone

                ivDelete.setOnClickListener {
                    onClickRemove(item)
                }
            }
        }
    }
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
        holder.bind(getItem(position))
    }


}