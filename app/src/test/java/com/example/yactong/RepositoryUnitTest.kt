package com.blackcows.butakaeyak

import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.repository.DrugRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertEquals
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
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//TODO: Java 17 is Required to Run this Code.

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class RepositoryUnitTest {
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
    fun apiDrugTest() = runBlocking {
        val result = suspendCoroutine { continuation ->
            drugRepository.searchDrugs("한미아스피린") { drugs ->
                // callback 내용 작성
                continuation.resume(drugs)
            }
        }

        assertNotNull(result)
        assertTrue(result.isNotEmpty())

        result.forEachIndexed { i, it ->
            println("$i: ${it.name}")
        }
    }



    @Test
    fun apiPillTest() = runBlocking {
        val result = suspendCoroutine { continuation ->
            drugRepository.searchPills("타이레놀") { pills ->
                // callback 내용 작성
                continuation.resume(pills)
            }
        }

        assertNotNull(result)
        assertTrue(result.isNotEmpty())

        result.forEachIndexed { i, it ->
            println("$i: ${it.name}")
        }
    }

    @Test
    fun getDrugsFromApi() = runBlocking {
        println("--------------------------------------------------")
        for(i in 1..10) {
            val result = drugRepository.searchDrugs("", i, 10)
            result.forEachIndexed {j, it ->
                print("(${(i-1)*10 + j}): ${it.name}, ${it.id}     ")
            }
            println()
        }
        println("--------------------------------------------------")
    }
    /*
(0): 활명수, 195700020     (1): 신신티눈고(살리실산반창고)(수출명:SINSINCORNPLASTER), 195900034     (2): 아네모정, 195900043     (3): 타치온정50밀리그램(글루타티온(환원형)), 197100015     (4): 겔포스현탁액(인산알루미늄겔), 197400207     (5): 일양노이겔현탁액(규산알루민산마그네슘), 197700189     (6): 일양노이시린에이정(규산알루민산마그네슘), 197800242     (7): 자모, 195900045     (8): 페니라민정(클로르페니라민말레산염), 196000011     (9): 삐콤정, 196200034
(10): 세나서트2밀리그람질정, 198401161     (11): 겔마현탁액, 199001305     (12): 게루삼정, 196400046     (13): 보화소합원(대환,소환), 196400089     (14): 지엘타이밍정(카페인무수물), 196500051     (15): 베스타제정, 196600011     (16): 베스타제당의정, 196600012     (17): 대한염화나트륨액, 199100317     (18): 옵타젠트점안액(포비돈), 199100655     (19): 1.영진구론산바몬드액2.영진구론산바몬드스파클링액, 199200813
(20): 에바치온캡슐(글루타티온), 199202273     (21): 원비디, 196600063     (22): 에어신신파스, 196700061     (23): 후라베린큐시럽, 196900042     (24): 코푸시럽에스, 196900058     (25): 엔클비액(염화나트륨), 199303012     (26): 지노콜시럽(구연산부타미레이트), 199400693     (27): 판콜에이내복액, 196800036     (28): 아로나민골드정, 197000037     (29): 제로미아액, 199502669
(30): 성광멸균바세린거즈(백색바셀린), 199600443     (31): 대웅우루사연질캡슐, 197000040     (32): 액티피드정, 197000053     (33): 보나링에이정(디멘히드리네이트), 197000076     (34): 미보(MEBO)연고, 199604959     (35): 한림포비돈점안액(수출명:한비돈점안액), 199803472     (36): 건위황정, 197000208     (37): 쎄레스톤지크림, 197100059     (38): 키모랄에스정, 197100081     (39): 기모타부정(수출명:케이티정), 197200084
(40): 포스테리산좌제, 199806423     (41): 포스테리산연고, 199806424     (42): 뇌선, 197200483     (43): 성광관장약, 197300075     (44): 안국니트로푸라존연고, 197400199     (45): 로와치넥스캡슐, 200009088     (46): 잘젠정, 200101844     (47): 태극아즈렌에스연고(구아야줄렌), 200200836     (48): 마그밀정(수산화마그네슘), 197400246     (49): 트레스탄캡슐, 197400262
(50): 뮤코졸정(브롬헥신염산염), 197400277     (51): 신일브롬헥신염산염정(수출명 : BIVOTUME, BIVO), 197400343     (52): 프리비투스현탁액(레보클로페라스틴펜디조산염), 200201397     (53): 노루모현탁액, 200300960     (54): 유락신연고(크로타미톤), 197400416     (55): 낙센정(나프록센), 197500016     (56): 이벤캡슐, 200301479     (57): 레보골드액, 200301508     (58): 암포젤정(건조수산화알루미늄겔), 197500088     (59): 포타딘액(포비돈요오드), 197600172
(60): 액티피드시럽, 197600174     (61): 티어드롭점안액2%(포비돈), 200301820     (62): 리렌스연고(구아야줄렌), 200402866     (63): 태극아즈렌에스크림(구아야줄렌), 200501105     (64): 페리나정, 197600345     (65): 로프민캡슐(로페라미드염산염), 197600412     (66): 신일비사코딜정, 197600483     (67): 코네티비나겔(히알루론산나트륨), 200511332     (68): 코네티비나거즈(히알루론산나트륨), 200603947     (69): 제스판골드정, 200709913
(70): 세가톤트로키(염화세칠피리디늄), 197600539     (71): 갈타제산(베타-갈락토시다제(아스퍼길루스)), 197600614     (72): 리나치올시럽(카르보시스테인)(수출명:HyundiolSyrup), 197600722     (73): 안티비오과립75밀리그람(락토바실루스아시도필루스)(수출명: 안티비오프로(Antibio Pro)), 197700312     (74): 큐앤큐바셀린윤나거즈, 200800378     (75): 케어번연고(구아야줄렌), 200903570     (76): 부루펜정200밀리그램(이부프로펜), 197700120     (77): 액티페린정, 197700322     (78): 핑크프로캡슐, 200904391     (79): 데카키논캡슐(유비데카레논캡슐), 197800027
(80): 로파인캡슐(로페라미드염산염), 197800212     (81): 메코민캡슐(메코발라민), 197800391     (82): 베로타정, 200904579     (83): 파라스트캡슐, 200904649     (84): 다이제틴정, 200904689     (85): 리나치올캡슐375밀리그램(카르보시스테인)(수출명:HyundiolCapsule375Mg), 197800466     (86): 후시딘연고(퓨시드산나트륨), 197900171     (87): 알리코클로르헥시딘크림, 197900176     (88): 에코라연고(에코나졸질산염), 197900217     (89): 번세이프크림(구아야줄렌), 200905978
(90): 액티몰스액(시트룰린말산염), 200906270     (91): 마스질정(클레마스틴푸마르산염), 197900219     (92): 스파토민캡슐(디시클로민염산염), 197900411     (93): 비거스액(시트룰린말산염), 200906476     (94): 제로바액, 201301364     (95): 글루100정(글루타티온(환원형)), 201305220     (96): 나오덤크림(구아야줄렌), 201309340     (97): 게보린정(수출명:돌로린정), 197900277     (98): 포타딘연고(포비돈요오드), 198000168     (99): 아줄린연고(구아야줄렌), 201402801
     */
}