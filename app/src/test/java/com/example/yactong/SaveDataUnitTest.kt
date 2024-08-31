package com.example.yactong

import com.example.yactong.data.repository.DrugRepository
import com.example.yactong.data.retrofit.AiResultDto
import com.example.yactong.data.retrofit.Medicine
import com.example.yactong.data.save_raw.AiApiService
import com.example.yactong.data.save_raw.AiRequestDto
import com.example.yactong.data.save_raw.AiRetrofitClient
import com.example.yactong.data.save_raw.makeAiMessage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class SaveDataUnitTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var drugRepository: DrugRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        println("--------------------------------------------------------------------------------------")
    }

    @After
    fun after() {
        println("--------------------------------------------------------------------------------------")
    }

    @Test
    fun toJson() {

    }


    @Test
    fun getSummarize() = runBlocking {
        val instance = AiRetrofitClient.getInstance().create(AiApiService::class.java)
        val result = suspendCoroutine { continuation ->


            drugRepository.searchDrugs("타이레놀") { drugs ->
                // callback 내용 작성
                continuation.resume(drugs)
            }
        }

        val list = listOf(makeAiMessage(result))
        val response = instance.getSummarize(AiRequestDto(list))
        val medicineJson = response.choice[0].msg.medicine

        val gson = Gson()
        val type: Type = object : TypeToken<List<Medicine>>() {}.type
        val medicines: List<Medicine> = gson.fromJson(medicineJson, type)

        medicines.forEachIndexed { i, it->
            println("$i: ${it.name}, ${it.caution}")
        }

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    /*//TODO: 결과값 이걸로 데이터 뽑아내서 가져오기

     */


    @Test
    fun toObject() {
        val json =
            "{\n" +
                    "  \"id\": \"chatcmpl-A1uLUgZA77iN1uuIG3yNvdryIWzWo\",\n" +
                    "  \"object\": \"chat.completion\",\n" +
                    "  \"created\": 1725018284,\n" +
                    "  \"model\": \"gpt-3.5-turbo-0125\",\n" +
                    "  \"choices\": [\n" +
                    "    {\n" +
                    "      \"index\": 0,\n" +
                    "      \"message\": {\n" +
                    "        \"role\": \"assistant\",\n" +
                    "        \"content\": [\n" +
                    "          {\n" +
                    "            \"id\": \"202005623\",\n" +
                    "            \"name\": \"어린이타이레놀산160밀리그램(아세트아미노펜)\",\n" +
                    "            \"enterprise\": \"한국존슨앤드존슨판매(유)\",\n" +
                    "            \"effect\": \"감기로 인한 발열 및 동통(통증), 두통, 신경통, 근육통, 월경통, 염좌통, 치통, 관절통, 류마티양 동통에 사용\",\n" +
                    "            \"instructions\": \"만 7~12세 소아는 1회 권장용량을 4~6시간마다 필요 시 물 없이 혀에 직접 복용\",\n" +
                    "            \"warning\": \"매일 세잔 이상 정기적 음주자가 이 약 또는 다른 해열진통제를 복용할 때는 의사 또는 약사와 상의\",\n" +
                    "            \"caution\": \"과민증, 소화성궤양, 심한 혈액 이상, 심한 간장애, 심한 신장(콩팥)장애, 심한 심장기능저하 환자, 아스피린 천식 또는 경험자는 복용 금지\",\n" +
                    "            \"interaction\": \"바르비탈계 약물, 삼환계 항우울제, 알코올, 다른 소염진통제와 함께 복용 금지\",\n" +
                    "            \"sideEffect\": \"쇽, 아나필락시양 증상, 천식발작, 혈소판 감소, 과립구감소, 용혈성빈혈, 메트헤모글로빈혈증, 혈소판기능 저하, 청색증, 과민증상, 구역, 구토, 식욕부진, 장기복용시 위장출혈, 소화성궤양, 천공 등의 위장관계 이상반응, 발진, 알레르기 반응, 피부점막안증후군, 독성표피괴사용해 등\",\n" +
                    "            \"storingMethod\": \"실온에서 보관, 어린이 금지\",\n" +
                    "            \"openDate\": \"2023-07-12\",\n" +
                    "            \"updateDate\": \"2023-07-12\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"id\": \"202106092\",\n" +
                    "            \"name\": \"타이레놀정500밀리그람(아세트아미노펜)\",\n" +
                    "            \"enterprise\": \"한국존슨앤드존슨판매(유)\",\n" +
                    "            \"effect\": \"감기로 인한 발열 및 동통, 두통, 신경통, 근육통, 월경통, 염좌통, 치통, 관절통, 류마티양 동통에 사용\",\n" +
                    "            \"instructions\": \"만 12세 이상 소아 및 성인은 1회 1~2정씩, 1일 3~4회 필요시 복용\",\n" +
                    "            \"warning\": \"매일 세 잔 이상 정기적 음주자가 이 약 또는 다른 해열진통제를 복용할 때는 의사 또는 약사와 상의\",\n" +
                    "            \"caution\": \"과민증, 소화성궤양, 심한 혈액 이상, 간장애, 신장(콩팥)장애, 심장기능저하 환자, 아스피린 천식 환자 또는 경험자는 복용 금지\",\n" +
                    "            \"interaction\": \"바르비탈계 약물, 삼환계 항우울제 및 알코올을 투여한 환자, 와파린, 플루클록사실린을 복용하는 환자는 의사 또는 약사와 상의\",\n" +
                    "            \"sideEffect\": \"쇽 증상, 천식발작, 혈소판감소, 과립구감소, 용혈성빈혈, 메트헤모글로빈혈증, 혈소판기능 저하, 청색증, 과민증상, 구역, 구토, 식욕부진, 위장출혈, 소화성궤양, 천공, 발진, 알레르기 반응, 피부점막안증후군, 독성표피괴사증, AST 상승, ALT 상승, 고정발진 등\",\n" +
                    "            \"storingMethod\": \"실온에서 보관, 어린이의 손이 닿지 않는 곳에 보관\",\n" +
                    "            \"openDate\": \"2023-05-31\",\n" +
                    "            \"updateDate\": \"2023-11-03\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"id\": \"202106954\",\n" +
                    "            \"name\": \"타이레놀콜드-에스정\",\n" +
                    "            \"enterprise\": \"한국존슨앤드존슨판매(유)\",\n" +
                    "            \"effect\": \"감기의 제증상(콧물, 코막힘, 재채기, 인후통, 기침, 오한, 발열, 두통, 관절통, 근육통)의 완화에 사용\",\n" +
                    "            \"instructions\": \"만 15세 이상은 1회 1정씩, 1일 3회 식후 30분에 복용\",\n" +
                    "            \"warning\": \"매일 세 잔 이상 정기적 음주자가 이 약 또는 다른 해열진통제를 복용할 때는 의사 또는 약사와 상의\",\n" +
                    "            \"caution\": \"과민증 또는 경험자, 다른 해열진통제, 감기약 복용 시 천식을 일으킨 적이 있는 사람, 만 3개월 미만의 영아, MAO억제제를 복용하고 있거나 복용을 중단한 후 2주 이내의 사람은 복용 금지\",\n" +
                    "            \"interaction\": \"MAO 억제제, 진해거담제, 다른 감기약, 해열진통제, 진정제, 항히스타민제를 함유하는 내복약과 함께 복용 금지\",\n" +
                    "            \"sideEffect\": \"발진, 가려움, 구역, 구토, 식욕부진, 변비, 부종, 배뇨곤란, 지속적이거나 심한 목마름, 어지러움, 불안, 떨림, 불면, 뇌혈관사고, 감각이상, 심근경색증, 허혈성 대장염, 쇽 증상, 피부점막안증후군, 독성표피괴사용해, 급성 전신성 발진성 농포증, 발열, 홍반, 천식, 전신의 나른함, 황달, 간질성폐렴, 고정발진 등\",\n" +
                    "            \"storingMethod\": \"습기와 빛을 피해 실온에서 보관, 어린이의 손이 닿지 않는 곳에 보관\",\n" +
                    "            \"openDate\": \"2023-06-12\",\n" +
                    "            \"updateDate\": \"2023-08-10\"\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      },\n" +
                    "      \"logprobs\": null,\n" +
                    "      \"finish_reason\": \"stop\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"usage\": {\n" +
                    "    \"prompt_tokens\": 6547,\n" +
                    "    \"completion_tokens\": 2111,\n" +
                    "    \"total_tokens\": 8658\n" +
                    "  },\n" +
                    "  \"system_fingerprint\": null\n" +
                    "}"
        val gson = GsonBuilder().create()
        val dto = gson.fromJson(json, AiResultDto::class.java)

        val medicineJson = dto.choice[0].msg.medicine
        val type: Type = object : TypeToken<List<Medicine>>() {}.type
        val medicines: List<Medicine> = gson.fromJson(medicineJson, type)

        medicines.forEachIndexed { i, it->
            println("$i: ${it.name}, ${it.caution}")
        }
    }
}