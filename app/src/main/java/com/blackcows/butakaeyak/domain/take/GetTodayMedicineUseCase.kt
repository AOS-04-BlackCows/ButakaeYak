package com.blackcows.butakaeyak.domain.take

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime
import io.ktor.util.date.WeekDay
import javax.inject.Inject

class GetTodayMedicineUseCase @Inject constructor(
    private val localRepository: LocalRepository
){
    operator fun invoke(
        weekDay: WeekDay,
        callback: (List<MedicineAtTime>) -> (Unit)) {

        val result = mutableListOf<MedicineAtTime>()
        val timeToMedicines = mutableMapOf<String, MutableList<Medicine>>()

        localRepository.getMyMedicines().forEach {  myMedicine ->
            val todayPlans = myMedicine.alarms[weekDay]
            todayPlans?.forEach { time ->
                timeToMedicines
                    .getOrPut(time) { mutableListOf() }
                    .add(myMedicine.medicine)
            }
        }
        timeToMedicines.forEach { (k, v) ->
            result.add(MedicineAtTime(weekDay, k, v))
        }

        callback(result)
    }
}