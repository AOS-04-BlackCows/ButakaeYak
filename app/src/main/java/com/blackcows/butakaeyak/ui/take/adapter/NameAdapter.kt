package com.blackcows.butakaeyak.ui.take.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.HomeRvGroup
import com.blackcows.butakaeyak.data.models.MedicineDetail
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.TakeAddMedicine
import com.blackcows.butakaeyak.databinding.ItemHomeTodayMedicineGroupBinding
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewNameBinding
import com.blackcows.butakaeyak.ui.note.recycler.RecyclerItem
import com.blackcows.butakaeyak.ui.take.FormSelectDialog
import com.blackcows.butakaeyak.ui.take.adapter.NameAdapter.DetailViewHolder
import com.bumptech.glide.Glide

class NameAdapter(private val clickListener: ClickListener): ListAdapter<TakeAddMedicine, DetailViewHolder>(
    object: DiffUtil.ItemCallback<TakeAddMedicine>() {
        override fun areItemsTheSame(oldItem: TakeAddMedicine, newItem: TakeAddMedicine): Boolean {
            return (oldItem == newItem)
        }
        override fun areContentsTheSame(oldItem: TakeAddMedicine, newItem: TakeAddMedicine): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class DetailViewHolder(private val binding: ItemRecyclerviewNameBinding): ViewHolder(binding.root) {
        fun bind(item: TakeAddMedicine) {
            with(binding){
                Glide.with(root.context)
                    .load(item.imageUrl)
                    .into(btnMedicineForm)
                btnMinus.setOnClickListener {
                    clickListener.onMinusClick(item)
                }
                etMedicineName.setText(item.name)
                clMedicineForm.setOnClickListener {
                    FormSelectDialog(root.context, object :
                        FormSelectDialog.OnFormSelectListener {
                        override fun onFormSelected(image: Drawable) {
                            Glide.with(root.context)
                                .load(image)
                                .into(btnMedicineForm)
                            Log.d("imageLink", "$image")
                        }
                    }, btnMedicineForm.background).show()
                }
                btnMinus.setOnClickListener {
                    clickListener.onMinusClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_name, parent, false)
        return DetailViewHolder(ItemRecyclerviewNameBinding.bind(view))
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface ClickListener {
        fun onMinusClick(item: TakeAddMedicine)
        fun onSearchClick(item: TakeAddMedicine)
    }
}