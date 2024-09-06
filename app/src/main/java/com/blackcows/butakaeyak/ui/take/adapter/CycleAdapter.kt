package com.blackcows.butakaeyak.ui.take.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewCycleBinding
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.blackcows.butakaeyak.ui.take.data.AlarmItem

class CycleAdapter(private val context: Context, val alarmList: MutableList<AlarmItem>) : RecyclerView.Adapter<CycleAdapter.CycleViewHolder>() {

    private var onDaySelectedListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CycleViewHolder {
        val binding = ItemRecyclerviewCycleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CycleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CycleViewHolder, position: Int) {
        val alarmItem = alarmList[position]

        holder.timeText.text = alarmItem.timeText
        holder.image
        holder.dayClickListeners()
        holder.binding.ivDelete.setOnClickListener {
            removeAt(position)
        }
        holder.timeText.setOnClickListener {
            TimePickerDialog(context,holder.timeText).show()
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    inner class CycleViewHolder(val binding : ItemRecyclerviewCycleBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.ivCycleOneAlarm
        val timeText = binding.tvCycleOneAlarmTime
        val monday = binding.tvMon
        val tuesday = binding.tvTue
        val wednesday = binding.tvWed
        val thursday = binding.tvThu
        val friday = binding.tvFri
        val saturday = binding.tvSat
        val sunday = binding.tvSun

        fun dayClickListeners() {
            monday.setOnClickListener { selectDay(monday) }
            tuesday.setOnClickListener { selectDay(tuesday) }
            wednesday.setOnClickListener { selectDay(wednesday) }
            thursday.setOnClickListener { selectDay(thursday) }
            friday.setOnClickListener { selectDay(friday) }
            saturday.setOnClickListener { selectDay(saturday) }
            sunday.setOnClickListener { selectDay(sunday) }
        }

        private fun selectDay(textView: TextView) {
            val isSelected = textView.currentTextColor == ContextCompat.getColor(itemView.context, R.color.green)
            textView.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (isSelected) R.color.dark_gray else R.color.green
                )
            )
            onDaySelectedListener?.invoke()
        }
    }

    fun removeAt(position: Int) {
        alarmList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, alarmList.size)
    }

    fun setOnDaySelectedListener(listener: () -> Unit) {
        this.onDaySelectedListener = listener
    }
}