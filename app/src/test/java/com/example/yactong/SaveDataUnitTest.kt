package com.example.yactong

import com.example.yactong.data.repository.DrugRepository
import com.example.yactong.data.retrofit.ApiBaseUrl
import com.example.yactong.data.retrofit.RetrofitClient
import com.example.yactong.data.save_raw.AiApiService
import com.example.yactong.data.save_raw.AiRequestDto
import com.example.yactong.data.save_raw.AiRetrofitClient
import com.example.yactong.data.save_raw.makeAiMessage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
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
        println(response.string())

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    /*//TODO: 결과값 이걸로 데이터 뽑아내서 가져오기
    {
  "id": "chatcmpl-A1qhDiqRnRvBaVE5SACHt0YeBKoYS",
  "object": "chat.completion",
  "created": 1725004255,
  "model": "gpt-3.5-turbo-0125",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "{\n    \"medicine\": [\n        {\n            \"effect\": [\"식욕감퇴\", \"위부팽만감\", \"소화불량\", \"과식\", \"구역\", \"구토\"],\n            \"instructions\": [\"연령별 용량\", \"1일 3회\", \"식후\", \"4시간 간격\"],\n            \"warning\": [\"만 3개월 미만 금지\", \"나트륨 제한\", \"과민증 환자 주의\", \"용법 준수\"],\n            \"storingMethod\": [\"실온 보관\", \"습기 및 빛 피하기\", \"어린이 금지\"]\n        },\n        {\n            \"effect\": [\"감기로 인한 발열 및 동통\", \"두통\", \"신경통\", \"근육통\", \"월경통\", \"염좌통\", \"치통\", \"관절통\", \"류마티양 동통\"],\n            \"instructions\": [\"만 7~12세 소아는 1회 권장용량을 4~6시간마다 필요 시 물 없이 혀에 직접 복용\", \"가능한 최단기간동안 최소 유효용량으로 복용\", \"1일 최대 5회 (75 mg/kg) 초과 복용 금지\"],\n            \"warning\": [\"매일 세잔 이상 정기적 음주자 복용 시 의사 또는 약사와 상의\", \"피부발진 또는 과민반응 징후 발생 시 즉시 중단\", \"아세트아미노펜 일일 최대 용량 (4,000 mg) 초과 금지\"],\n            \"caution\": [\"과민증, 소화성궤양, 심한 혈액 이상 등 환자 복용 조심\", \"복용 전 의사 또는 약사 상의 필요\", \"통증이 10일 이상 (성인) 또는 5일 이상 (소아) 지속 시 의사 상의 필요\"],\n            \"interaction\": [\"다른 약물과 병용 시 상호작용 주의\"]\n        },\n        {\n            \"effect\": [\"감기의 제증상 완화\", \"콧물\", \"코막힘\", \"재채기\", \"인후통\", \"기침\", \"오한\", \"두통\", \"관절통\", \"근육통\"],\n            \"instructions\": [\"만 15세 이상은 1회 1정씩\", \"1일 3회 식후 30분에 복용\"],\n            \"warning\": [\"매일 세 잔 이상 음주자는 의사 또는 약사 상의\", \"피부발진 또는 과민반응 징후 발생 시 즉시 중단\", \"아세트아미노펜 일일 최대 용량 (4,000 mg) 초과 금지\", \"중증 피부 이상반응 발생 시 중단\"],\n            \"caution\": [\"과민증 또는 경험자, 다른 해열진통제, 감기약 복용에 주의\", \"MAO억제제 복용자 등은 복용 금지\", \"용량 및 증상 주의\"],\n            \"interaction\": [\"다른 약물과 병용 시 상호작용 주의\"],\n            \"sideEffect\": [\"가려움, 구역, 구토 등\", \"발진·발적, 쇽 증상, 피부점막안증후군, 독성표피괴사증 등 발생 시 복용 중단\"],\n            \"storingMethod\": [\"습기와 빛을 피해 실온에서 보관\", \"어린이의 손길을 피하고 보관\"]\n        }\n    ]\n}",
        "refusal": null
      },
      "logprobs": null,
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 6253,
    "completion_tokens": 1068,
    "total_tokens": 7321
  },
  "system_fingerprint": null
}
     */
}