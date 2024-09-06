package com.blackcows.butakaeyak.ui.take.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewCycleBinding
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.blackcows.butakaeyak.ui.take.data.AlarmItem
import java.util.Calendar

class CycleAdapter(private val context: Context, private val alarmList: MutableList<AlarmItem>,private val onItemCountChangeListener: (Int) -> Unit) : RecyclerView.Adapter<CycleAdapter.CycleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CycleViewHolder {
        val binding = ItemRecyclerviewCycleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CycleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CycleViewHolder, position: Int) {
        holder.bind(alarmList[position])
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

        val timeText = binding.tvCycleOneAlarmTime
        fun bind(alarmItem: AlarmItem) {
            timeText.text = "알림 시간 : " + alarmItem.timeText ?: "시간을 설정하세요"
        }
    }

    fun getAlarmList(): List<AlarmItem> = alarmList

    fun addItem(alarmItem: AlarmItem) {
        alarmList.add(alarmItem)
        notifyDataSetChanged()
        onItemCountChangeListener(alarmList.size)
    }

    fun getTimeInMillis(hourOfDay: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }


    fun removeAt(position: Int) {
        if (position < alarmList.size) {
            alarmList.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
            notifyItemRangeChanged(position, alarmList.size)
            onItemCountChangeListener(alarmList.size)
        }
    }
}