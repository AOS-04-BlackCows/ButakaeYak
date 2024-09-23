package com.blackcows.butakaeyak.ui.textrecognition.data

import com.blackcows.butakaeyak.ui.textrecognition.OCRRetrofitClient

class OCRRepository {
    suspend fun createChatCompletion(request: OCRRequest): OCRResponse {
        return OCRRetrofitClient.openAiApi.createChatCompletion(request)
    }
// 아레 코드는 리팩토링하며 적용예정
//    suspend fun createChatCompletion(request: OCRResultDto) : OCRTextRecApiService{
//        return RetrofitClient.getInstance(ApiBaseUrl.GPTUrl).create(OCRTextRecApiService::class.java)
//    }
}