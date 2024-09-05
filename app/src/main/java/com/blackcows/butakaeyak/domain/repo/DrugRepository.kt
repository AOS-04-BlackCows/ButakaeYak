package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.models.Pill

@Deprecated("이거 말고 MedicineRepository 쓰세요!")
interface DrugRepository {
    fun searchDrugs(name: String, callback: (List<Drug>) -> (Unit))
    fun searchPills(name: String, callback: (List<Pill>) -> (Unit))
}