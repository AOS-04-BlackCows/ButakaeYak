package com.blackcows.butakaeyak.ui.take.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.TodayMedicineItemBinding
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime
import okhttp3.internal.notify

class TodayMedicineRvAdapter:
    ListAdapter<MedicineAtTime, TodayMedicineRvAdapter.MedicineAtTimeViewHolder>(
        object: DiffUtil.ItemCallback<MedicineAtTime>() {
            override fun areItemsTheSame(oldItem: MedicineAtTime, newItem: MedicineAtTime): Boolean {
                //Log.d("DiffUtil", "old: ${oldItem.imageUrl}, new: ${newItem.imageUrl}}")
                return  (oldItem.time == newItem.time) && (oldItem.list == newItem.list)
            }
            override fun areContentsTheSame(oldItem: MedicineAtTime, newItem: MedicineAtTime): Boolean {
                //Log.d("DiffUtil", "old is same with new: ${oldItem == newItem}")
                return oldItem == newItem
            }

        }
    ) {
    inner class MedicineAtTimeViewHolder(private val binding: TodayMedicineItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MedicineAtTime) {
//            GlideApp.with(binding.root)
//                .load(item.imageUrl)
//                .into(binding.image)
            with(binding) {
                alarmTimeTv.text = item.time

                val adapter = SimpleMedicineRvAdapter()
                medicineRv.run {
                    layoutManager = LinearLayoutManager(binding.root.context)
                    this.adapter = adapter
                    addItemDecoration(TakeRvDecorator.getLinearDecoSimpleItem())
                }

                adapter.submitList(item.list.take(2))
                expandButton.visibility = if (item.list.size > 2) View.VISIBLE else View.GONE

                expandButton.setOnClickListener {
                    foldButton.visibility = View.VISIBLE
                    expandButton.visibility = View.GONE
                    adapter.submitList(item.list.toMutableList())

                    Log.d("TodayMedicineRvAdapter", "list size: ${adapter.currentList.size}")
                }
                foldButton.setOnClickListener {
                    foldButton.visibility = View.GONE
                    expandButton.visibility = View.VISIBLE

                    adapter.submitList(item.list.take(2))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineAtTimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.today_medicine_item, parent, false)
        return MedicineAtTimeViewHolder(TodayMedicineItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MedicineAtTimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}