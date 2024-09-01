package com.example.yactong.data.save_raw

import com.example.yactong.BuildConfig
import com.example.yactong.data.dto.DrugInfoDto
import com.example.yactong.data.models.Drug
import com.example.yactong.data.retrofit.AiResultDto
import com.example.yactong.data.toMap
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.lang.reflect.Type

interface AiApiService {
    @POST("completions")
    suspend fun getSummarize(
        @Body requestDto: AiRequestDto
    ): AiResultDto
}

data class AiRequestDto(
    val messages: List<AiMessage>,
    val temperature: Float = 0.0f,
    val model: String = "gpt-3.5-turbo"     //gpt-4o-mini   //gpt-3.5-turbo
)


data class AiMessage(
    val role: String,
    val content: String,
)

fun makeAiMessage(list: List<Drug>): AiMessage {
    val role = "user"
    val prefix = "너는 JSON 요약 프로그램입니다. 아래의 JSON 배열을 간결하고 요약된 형식으로 변환해 주세요. \n" +
            "\n" +
            "### 대답 예시:\n" +
            "[\n" +
            "    {\n" +
            "        \"enterprise\": \"동화약품(주)\",\n" +
            "        \"name\": \"활명수\",\n" +
            "        \"id\": \"195700020\",\n" +
            "        \"effect\": \"식욕감퇴(식욕부진), 위부팽만감, 소화불량, 과식, 체함, 구역, 구토\",\n" +
            "        \"instructions\": \"연령별 용량, 1일 3회, 식후, 4시간 간격\",\n" +
            "        \"warning\": \"고령자 주의, 특정 질환자 주의\",\n" +
            "        \"caution\": \"만 3개월 미만 금지, 나트륨 제한, 과민증 환자 주의, 용법 준수\",\n" +
            "        \"interaction\": \"다른 약물과 상호작용 주의\",\n" +
            "        \"sideEffect\": \"드물게 발진, 알레르기 반응\",\n" +
            "        \"storingMethod\": \"실온 보관, 습기 및 빛 피하기, 어린이 금지\",\n" +
            "        \"itemImage\": \"이미지 없음\"\n" +
            "    },\n" +
            "    { ... },\n" +
            "    { ... }\n" +
            "]\n" +
            "지시 사항:\n" +
            "인삿말 및 수식어 제외: 대답에 인삿말이나 수식어를 포함하지 마세요.\n" +
            "필드 이름 변경: itemName 필드는 name으로 변경하세요. 나머지 필드는 예시와 같은 이름으로 변경해야 합니다.\n" +
            "핵심 정보 요약: 원래의 의미를 유지하면서, 핵심 정보를 짧고 명확하게 요약하세요. 각 항목은 한 줄로 작성합니다.\n" +
            "형식 유지: 결과는 JSON 배열 형식을 유지하고, 필드 이름을 정확히 변경하세요.\n" +
            "정확한 JSON 포맷: JSON 문자열의 구문이 정확해야 합니다. 모든 문자열은 따옴표로 둘러싸여 있어야 하고, 모든 객체와 배열은 올바르게 닫혀야 합니다.\n" +
            "아래 JSON 배열을 요약해 주세요"




    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    val strBuilder = StringBuilder().append(prefix)
    list.forEach {
        strBuilder
            .append("")
            .append(gson.toJson(it))
            .append("")
    }

    return AiMessage(role, strBuilder.toString())
}

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString, formatter)
    }
}