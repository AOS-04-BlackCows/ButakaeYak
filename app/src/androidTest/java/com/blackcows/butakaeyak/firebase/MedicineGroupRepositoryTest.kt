package com.blackcows.butakaeyak.firebase

import com.blackcows.butakaeyak.data.models.MedicineDetail
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.Memo
import com.blackcows.butakaeyak.data.source.api.MedicineInfoDataSource
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.domain.repo.MemoRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.ktor.util.date.WeekDay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import javax.inject.Inject

@HiltAndroidTest

class MedicineGroupRepositoryTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var medicineGroupRepository: MedicineGroupRepository
    @Inject
    lateinit var medicineInfoDataSource: MedicineInfoDataSource
    @Inject
    lateinit var memoRepository: MemoRepository

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
    fun integrate() {

    }

    @Test
    fun createGroup() = runBlocking {
        val medicine1 = medicineInfoDataSource.searchMedicines("타이레놀").getOrNull(0)
        val medicine2 = medicineInfoDataSource.searchMedicines("아미").getOrNull(0)
        val medicine3 = medicineInfoDataSource.searchMedicines("페").getOrNull(0)

        val list = mutableListOf<MedicineDetail>().apply {
            medicine1?.let { add(it) }
            medicine2?.let { add(it) }
            medicine3?.let { add(it) }
        }

        val newOne = MedicineGroup(
            id = "mediocritatem",
            name = "Celeste Cooke",
            userId = "erat",
            medicines = list,
            customNameList = listOf("비타민B", "비타민C"),
            imageUrlList = listOf(),
            startedAt = LocalDate.parse("2024-09-15"),
            finishedAt =LocalDate.parse("2024-09-22"),
            daysOfWeeks = listOf(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY),
            alarms = listOf("8:00", "12:00", "18:00"),
            hasTaken = listOf()
        )

        medicineGroupRepository.saveNewGroup(newOne)
    }

    @Test
    fun getMedicineGroup() = runBlocking {
        val userId = "erat"

        val groups = medicineGroupRepository.getMyGroups(userId)

        println("------------------------------------------")
        groups.forEach {
            println("${it.name}")
        }
        println("------------------------------------------")
    }

    @Test
    fun removeGroupTest() = runBlocking {
        val userId = "erat"

        val groups = medicineGroupRepository.getMyGroups(userId)

        println("------------------------------------------")
        groups.forEach {
            medicineGroupRepository.removeGroup(it)
        }
        println("------------------------------------------")
    }

    @Test
    fun addMemo() = runBlocking {
        val userId = "erat"
        val group = medicineGroupRepository.getMyGroups(userId)[0]

        val newMemo = Memo(
            id = "sapien",
            userId = "perpetua",
            group = group,
            content = "persequeris",
            createdAt = LocalDate.parse("2024-09-13"),
            updatedAt = LocalDate.parse("2024-09-15")
        )

        memoRepository.createMemo(newMemo)
    }

    @Test
    fun editMemo() = runBlocking {
        val userId = "erat"
        val group = medicineGroupRepository.getMyGroups(userId)[0]

        val memo = memoRepository.getMemoByMedicineGroupId(group.id)[0]
        val content = "hello!"

        memoRepository.editMemo(memo, content)
    }



    @Test
    fun removeMeo() = runBlocking {
        val userId = "erat"
        val group = medicineGroupRepository.getMyGroups(userId)[0]

        val memo = memoRepository.getMemoByMedicineGroupId(group.id)[0]

        memoRepository.deleteMemo(memo)
    }
}