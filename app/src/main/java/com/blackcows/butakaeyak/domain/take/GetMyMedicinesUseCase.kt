package com.blackcows.butakaeyak.domain.take

import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import javax.inject.Inject

class GetMyMedicinesUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    operator fun invoke(
        callback: (List<MyMedicine>) -> Unit) {
        val result = localRepository.getMyMedicines()
        callback(result)
    }
}