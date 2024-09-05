package com.blackcows.butakaeyak.ui.home.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentFeedListBinding
import com.blackcows.butakaeyak.databinding.ItemResultsBinding
import com.blackcows.butakaeyak.ui.home.data.ListItem
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment
import com.bumptech.glide.Glide

// 이전 코드 -                        values: MutableList<PlaceholderItem>
// 안되는 코드 class HomeRecyclerAdapter(private val onClick: (ListItem) -> Unit)
class HomeRecyclerAdapter(private val clickListener: ClickListener) :
    ListAdapter<Medicine, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

        //TODO itemClick 처리 이벤트
    private var itemClickListener: ((String) -> Unit)? = null

    fun setItemClickListener(listener: (String) -> Unit) {
        itemClickListener = listener
    }

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

        private const val TYPE_PIll = 0
        private const val TYPE_FEED = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Medicine -> TYPE_PIll
//            is ListItem.FeedItem -> TYPE_FEED
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PIll -> {
                val pillbinding =
                    ItemResultsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PillResultHolder(pillbinding)
            }
//            TYPE_FEED -> {}
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        runCatching {
            when (val item = getItem(position)) {
                is Medicine -> (holder as PillResultHolder).bind(item)
//                is ListItem.FeedItem -> (holder as FeedHolder).bind(item)
            }
        }.onFailure { exception ->
            Log.e("VideoListAdapter", "Exception! ${exception.message}")
        }
    }

    inner class PillResultHolder(pillView: ItemResultsBinding) :
        RecyclerView.ViewHolder(pillView.root) {
        private val ivPill: ImageView = pillView.ivPill
        private val tvPillname: TextView = pillView.tvPillname
        private val tvPilltype: TextView = pillView.tvPilltype
        private val btnMyPill: ToggleButton = pillView.btnMypill
        private val loPillinfo: ConstraintLayout = pillView.loPillinfo

        fun bind(pillitem: Medicine) {
            with(pillitem) {
                Glide.with(itemView).load(imageUrl?:R.drawable.medicine).into(ivPill)
                tvPillname.text = name
                tvPilltype.text = effect
                loPillinfo.setOnClickListener {
                    clickListener.onItemClick(pillitem)
                    Log.d("아이템 좋아요 누름", getItem(1).toString())
                }
                btnMyPill.setOnCheckedChangeListener { buttonView, isChecked ->
                    clickListener.onMyPillClick(pillitem,isChecked)
                    Log.d("아이템 복용약 누름","${name}")

                    //TODO btnMyPill 클릭 시 약 이름과 TakeAdd로 화면 이동
                    itemClickListener?.invoke(tvPillname.text.toString())
                    MainNavigation.addFragment(
                        TakeAddFragment.newInstance(pillitem), FragmentTag.TakeAddFragment
                    )
                }
            }
        }
    }

    inner class FeedHolder(feedView: FragmentFeedListBinding) :
        RecyclerView.ViewHolder(feedView.root) {
//        val feedthumb: ImageView = binding.feedIvThumb
//        val feedname: TextView = binding.feedTvName
//        val feedwriter: TextView = binding.feedTvWriter

        fun bind(pillitem: ListItem.FeedItem) {
            when (pillitem) {
            }

        }
    }
    interface ClickListener{
        fun onItemClick(item: Medicine)
        fun onMyPillClick(item: Medicine,needAdd :Boolean)
    }
}