package com.blackcows.butakaeyak.ui.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentSearchHistoryBinding
import com.blackcows.butakaeyak.databinding.ItemResultsBinding
import com.blackcows.butakaeyak.ui.search.data.ListItem
import com.bumptech.glide.Glide

private const val TAG = "홈 어뎁터"
class SearchRecyclerAdapter(private val clickListener: ClickListener) :
    ListAdapter<Medicine, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Medicine>() {
            override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
                return when {
                    oldItem is Medicine && newItem is Medicine ->
                        oldItem.id == newItem.id

//                    oldItem is ListItem.FeedItem && newItem is ListItem.FeedItem ->
//                        oldItem.name == newItem.name

                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
                return oldItem == newItem
            }
        }

        private const val TYPE_MEDICINE = 0
        private const val TYPE_FEED = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Medicine -> TYPE_MEDICINE
//            is ListItem.FeedItem -> TYPE_FEED
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MEDICINE -> {
                val medicineBinding =
                    ItemResultsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MedicineResultHolder(medicineBinding)
            }
//            TYPE_FEED -> {}
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        runCatching {
            when (val item = getItem(position)) {
                is Medicine -> (holder as MedicineResultHolder).bind(item)
//                is ListItem.FeedItem -> (holder as FeedHolder).bind(item)
            }
        }.onFailure { exception ->
            Log.e("VideoListAdapter", "Exception! ${exception.message}")
        }
    }

    inner class MedicineResultHolder(medicineView: ItemResultsBinding) :
        RecyclerView.ViewHolder(medicineView.root) {
        private val ivMedicine: ImageView = medicineView.ivMedicine
        private val tvMedicineName: TextView = medicineView.tvMedicineName
        private val tvMedicineType: TextView = medicineView.tvMedicineType
        private val ivMyMedicine: ImageView = medicineView.ivMyMedicine
        private val loMedicineInfo: ConstraintLayout = medicineView.loMedicineInfo

        fun bind(medicineItem: Medicine) {
            val isSaved = clickListener.isMedicineChecked(medicineItem)
            Log.d("HomeRecyclerView", "Name: ${medicineItem.name}, $isSaved")
            
            with(medicineItem) {

                if(imageUrl?.isNotEmpty() == true) {
                    Glide.with(itemView).load(imageUrl).into(ivMedicine)
                }

                Log.d(TAG, "ivMyMedicine.isEnabled: ${ivMyMedicine.isEnabled}")

                tvMedicineName.text = name
                tvMedicineType.text = effect
                loMedicineInfo.setOnClickListener {
                    clickListener.onItemClick(medicineItem)
                    Log.d(TAG, "${name}")
                }
                ivMyMedicine.setOnClickListener {
                    Log.d(TAG, "click!")
                    //clickListener.onMyMedicineClick(medicineItem,isChecked)
                    clickListener.setMedicineChecked(medicineItem, true)
                    Log.d(TAG,"${name}")
                    //TODO NameFragment로 약 이름 넘기기
                }
            }
        }
    }

    inner class SearchHistoryHolder(searchHistory: FragmentSearchHistoryBinding) :
        RecyclerView.ViewHolder(searchHistory.root) {
//        val feedthumb: ImageView = binding.feedIvThumb
//        val feedname: TextView = binding.feedTvName
//        val feedwriter: TextView = binding.feedTvWriter

        fun bind(medicineitem: ListItem.FeedItem) {
            when (medicineitem) {
            }

        }
    }
    interface ClickListener{
        fun onItemClick(item: Medicine)
        fun isMedicineChecked(item: Medicine) : Boolean
        fun setMedicineChecked(item: Medicine, isChecked:Boolean)
//        fun onMyMedicineClick(item: Medicine,needAdd :Boolean)
    }
}