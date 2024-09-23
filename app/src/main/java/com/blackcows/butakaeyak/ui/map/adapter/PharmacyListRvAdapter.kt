package com.blackcows.butakaeyak.ui.map.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewPharmacyListBinding

class PharmacyListRvAdapter(private val clickListener: ClickListener): ListAdapter<KakaoPlacePharmacy, PharmacyListRvAdapter.KakaoPlacePharmacyListViewHolder>(
        object: DiffUtil.ItemCallback<KakaoPlacePharmacy>() {
            override fun areItemsTheSame(oldItem: KakaoPlacePharmacy, newItem: KakaoPlacePharmacy): Boolean {
                //Log.d("DiffUtil", "old: ${oldItem.imageUrl}, new: ${newItem.imageUrl}}")
                return  (oldItem.id == newItem.id)
            }
            override fun areContentsTheSame(oldItem: KakaoPlacePharmacy, newItem: KakaoPlacePharmacy): Boolean {
                //Log.d("DiffUtil", "old is same with new: ${oldItem == newItem}")
                return oldItem == newItem
            }

        }
) {

    inner class KakaoPlacePharmacyListViewHolder(private val binding: ItemRecyclerviewPharmacyListBinding): ViewHolder(binding.root) {
        fun bind(item: KakaoPlacePharmacy, position: Int) {
            with(binding) {

                tvPharmacyDistance.text = "${item.distance}m"
                tvPharmacyName.text = item.placeName
                tvPharmacyTelNum.text = item.phone
//                placeUrl.text = pharmacyItem.placeUrl
//                tvPharmacyAddress.text = item.addressName
                tvPharmacyRoadAddress.text = item.roadAddressName

                // 지도에서 보기
                tvPharmacyName.setOnClickListener {
                    clickListener.onViewPharmacyDetail(item)
                }
                // TODO 아이디로 로그인 해도 즐겨 찾기한 약국은 SharedPreference 에 추가 해야함.
                if (LocalDataSource(this.root.context).isPharmacyChecked(item.id)) {
                    btnFavoritePharmacy.setImageResource(R.drawable.icon_favorite_active)
                } else {
                    btnFavoritePharmacy.setImageResource(R.drawable.icon_favorite)
                }
                btnFavoritePharmacy.setOnClickListener {
                    clickListener.onFavoriteClick(item, position)
                    if (LocalDataSource(this.root.context).isPharmacyChecked(item.id)) {
                        btnFavoritePharmacy.setImageResource(R.drawable.icon_favorite_active)
                    } else {
                        btnFavoritePharmacy.setImageResource(R.drawable.icon_favorite)
                    }
                }
                btnCallPharmacy.setOnClickListener {
                    clickListener.onCallClick(item)
                }
                btnFindRoad.setOnClickListener {
                    clickListener.onFindRoadClick(item)
                }
            }
        }
    }
    var itemClick : ClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KakaoPlacePharmacyListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_pharmacy_list, parent, false)
        return KakaoPlacePharmacyListViewHolder(ItemRecyclerviewPharmacyListBinding.bind(view))
    }

    override fun onBindViewHolder(holder: KakaoPlacePharmacyListViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    interface ClickListener {
        fun onFavoriteClick(item: KakaoPlacePharmacy, position: Int)
        fun onCallClick(item: KakaoPlacePharmacy)
        fun onFindRoadClick(item: KakaoPlacePharmacy)
        fun onViewPharmacyDetail(item: KakaoPlacePharmacy)
    }

}