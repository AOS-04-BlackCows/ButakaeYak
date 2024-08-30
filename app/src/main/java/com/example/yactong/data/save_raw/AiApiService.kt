package com.example.yactong.data.save_raw

import com.example.yactong.BuildConfig
import com.example.yactong.data.dto.DrugInfoDto
import com.example.yactong.data.models.Drug
import com.example.yactong.data.toMap
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Query

interface AiApiService {
    @POST()
    suspend fun getSummarize(
        @Query("messages") msg: AiMessage,
        @Query("model") model: String = "pt-3.5-turbo"
    ): ResponseBody
}

data class AiMessage(
    val role: String,
    val content: String,
)

fun makeAiMessage(list: List<Drug>): AiMessage {
    val role = "user"
    val prefix = "다음 데이터를 기반으로 각 항목을 아래와 같이 요약해 주세요:\n" +
            "\n" +
            "- 효과: 식욕감퇴, 위부팽만감, 소화불량, 과식, 구역, 구토\n" +
            "- 복용법: 연령별 용량, 1일 3회, 식후, 4시간 간격\n" +
            "- 주의사항: 만 3개월 미만 금지, 나트륨 제한, 과민증 환자 주의, 용법 준수\n" +
            "- 보관방법: 실온 보관, 습기 및 빛 피하기, 어린이 금지\n" +
            "\n" +
            "약물 데이터 예시:\n" +
            "{\n" +
            "    \"entpName\": \"동화약품(주)\",\n" +
            "    \"itemName\": \"활명수\",\n" +
            "    \"itemSeq\": \"195700020\",\n" +
            "    \"efcyQesitm\": \"식욕감퇴(식욕부진), 위부팽만감, 소화불량, 과식, 체함, 구역, 구토\",\n" +
            "    \"useMethodQesitm\": \"연령별 용량, 1일 3회, 식후, 4시간 간격\",\n" +
            "    \"atpnWarnQesitm\": \"고령자 주의, 특정 질환자 주의\",\n" +
            "    \"atpnQesitm\": \"만 3개월 미만 금지, 나트륨 제한, 과민증 환자 주의, 용법 준수\",\n" +
            "    \"intrcQesitm\": \"다른 약물과 상호작용 주의\",\n" +
            "    \"seQesitm\": \"드물게 발진, 알레르기 반응\",\n" +
            "    \"depositMethodQesitm\": \"실온 보관, 습기 및 빛 피하기, 어린이 금지\",\n" +
            "    \"openDe\": \"2021-01-29 00:00:00\",\n" +
            "    \"updateDe\": \"2024-05-09\",\n" +
            "    \"itemImage\": \"이미지 없음\",\n" +
            "    \"bizrno\": \"1108100102\"\n" +
            "}\n" +
            "\n" +
            "위 데이터를 참고하여 각 항목을 키워드로 요약해 주세요. 가능한 한 간결하게 작성해 주세요."

    val gson = Gson()
    val strBuilder = StringBuilder().append(prefix)
    list.forEach {
        strBuilder
            .append("\n")
            .append(gson.toJson(it))
            .append("\n")
    }

    return AiMessage(role, strBuilder.toString())
}