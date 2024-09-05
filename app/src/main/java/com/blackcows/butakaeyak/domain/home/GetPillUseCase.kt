package com.blackcows.butakaeyak.domain.home

import com.blackcows.butakaeyak.data.models.Pill
import com.blackcows.butakaeyak.domain.repo.DrugRepository
import javax.inject.Inject

class GetPillUseCase @Inject constructor(
    private val drugRepository: DrugRepository
) {
    operator fun invoke(
        name: String,     //필요한 프로퍼티 넣기
        callback: (List<Pill>) -> (Unit)) {     // 람다로 callback 함수 넣기
        drugRepository.searchPills(name, callback)
    }
}