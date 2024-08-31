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
    val model: String = "gpt-3.5-turbo"
)


data class AiMessage(
    val role: String,
    val content: String,
)

fun makeAiMessage(list: List<Drug>): AiMessage {
    val role = "user"
    val prefix = "너는 ai가 아닌 Json 요약 프로그램이야. \n"+
            "내가 보낸 json 포맷의 각 필드를 요약하여 아래와 같은 방식으로 알려주면 돼.\n" +
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
            "    {\n" +
            "        \"enterprise\": \"한미약품(주)\",\n" +
            "        \"name\": \"진통제\",\n" +
            "        \"id\": \"123456789\",\n" +
            "        \"effect\": \"두통, 근육통, 관절통, 발열\",\n" +
            "        \"instructions\": \"성인은 1회 1정, 1일 3회, 식후 복용\",\n" +
            "        \"warning\": \"간 질환자 주의, 장기 복용 주의\",\n" +
            "        \"caution\": \"임산부, 수유부는 복용 전 의사 상담\",\n" +
            "        \"interaction\": \"다른 진통제와 병용 시 주의\",\n" +
            "        \"sideEffect\": \"위장 장애, 어지러움, 발진\",\n" +
            "        \"storingMethod\": \"습기와 빛을 피해 실온 보관\",\n" +
            "        \"itemImage\": \"이미지 없음\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"enterprise\": \"삼성제약(주)\",\n" +
            "        \"name\": \"해열제\",\n" +
            "        \"id\": \"987654321\",\n" +
            "        \"effect\": \"발열, 두통, 근육통 완화\",\n" +
            "        \"instructions\": \"성인은 1회 1정, 1일 4회, 식후 복용\",\n" +
            "        \"warning\": \"간 질환자, 신장 질환자 주의\",\n" +
            "        \"caution\": \"알레르기 반응 시 복용 금지\",\n" +
            "        \"interaction\": \"다른 해열제와 병용 시 주의\",\n" +
            "        \"sideEffect\": \"위장 불편, 발진\",\n" +
            "        \"storingMethod\": \"서늘한 곳에 보관\",\n" +
            "        \"itemImage\": \"이미지 없음\"\n" +
            "    }\n" +
            "]" +
            "\n" +
            "\n" +
            "위 예시를 참고하여 아래 조건을 만족시켜줘.\n" +
            "조건1. 인삿말 및 다른 수식언을 절대 대답에 포함시키지 않기\n" +
            "조건2. itemName 필드는 요약하지 않고 필드이름만 name으로 변경\n" +
            "조건3. 그 밖의 필드는 이름을 위 예시와 같이 변경하여 꼭 포함시키기"




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