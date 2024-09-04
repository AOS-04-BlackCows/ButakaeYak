package com.blackcows.butakaeyak.ui.home.data

import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.blackcows.butakaeyak.data.models.Medicine
import org.junit.runner.manipulation.Ordering.Context

class DataSource {
    companion object{
        private var INSTANCE : DataSource? = null

        fun getDataSoures() : DataSource{
            // DatoSource::class 객체에 lock을 걸어 한번에 한 스레드에서만 실행 되도록 함
            return synchronized(DataSource::class) {
                // 싱글톤 객체를 한번 호출하고 없으면 DataSource반환, 있으면 생성된 인스턴스 반환
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }

        //SharedPreferences
        fun saveData(context: android.content.Context, key:String, data: Set<String>) {
            val edit = context?.getSharedPreferences(key, MODE_PRIVATE)?.edit()
            edit!!.putStringSet(data.first(),data)
            edit!!.apply() // 저장완료
        }

//        fun loadData() {
//            val pref = getSharedPreferences("pref", MODE_PRIVATE)
//            binding.mainEtTitleEdit.setText(pref.getString("name",""))
//        }
    }

    // MVVM패턴에서 Model에 해당한다고 볼 수 있음
    fun getPillList() : List<Medicine>{
        // 만들어놓은 데이터클래스 리턴
        return CardDataList()
    }
    fun CardDataList() : ArrayList<Medicine>{
        return arrayListOf(
            Medicine(
                "000001",
                "약약",
                "더미덤",
                "더미 1",
                "초코"
            ),
            Medicine(
                "000002",
                "약약",
                "더미덤",
                "더미 2",
                "초코"
            ),
            Medicine(
                "000003",
                "약약",
                "더미덤",
                "더미 3",
                "초코"
            ),
            Medicine(
                "000004",
                "약약",
                "더미덤",
                "더미 4",
                "초코"
            ),
            Medicine(
                "000005",
                "약약",
                "더미덤",
                "더미 5",
                "초코"
            ),
            Medicine(
                "000006",
                "약약",
                "더미덤",
                "더미 6",
                "초코"
            ),
            Medicine(
                "000007",
                "약약",
                "더미덤",
                "더미 7",
                "초코"
            ),
            Medicine(
                "000008",
                "약약",
                "더미덤",
                "더미 8",
                "초코"
            ),
            Medicine(
                "000009",
                "약약",
                "더미덤",
                "더미 9",
                "초코"
            ),
            Medicine(
                "000010",
                "약약",
                "더미덤",
                "더미10",
                "초코"
            ),
            Medicine(
                "000011",
                "약약",
                "더미덤",
                "더미11",
                "초코"
            ),
            Medicine(
                "000012",
                "약약",
                "더미덤",
                "더미12",
                "초코"
            ),
            Medicine(
                "000013",
                "약약",
                "더미덤",
                "더미13",
                "초코"
            ),
            Medicine(
                "000014",
                "약약",
                "더미덤",
                "더미14",
                "초코"
            ),
            Medicine(
                "000015",
                "약약",
                "더미덤",
                "더미15",
                "초코"
            ),
            Medicine(
                "000016",
                "약약",
                "더미덤",
                "더미16",
                "초코"
            ),
            Medicine(
                "000017",
                "약약",
                "더미덤",
                "더미17",
                "초코"
            ),
            Medicine(
                "000018",
                "약약",
                "더미덤",
                "더미18",
                "초코"
            ),
            Medicine(
                "000019",
                "약약",
                "더미덤",
                "더미19",
                "초코"
            ),
            Medicine(
                "000020",
                "약약",
                "더미덤",
                "더미20",
                "초코"
            ),
            Medicine(
                "000021",
                "약약",
                "더미덤",
                "더미21",
                "초코"
            ),
            Medicine(
                "000022",
                "약약",
                "더미덤",
                "더미22",
                "초코"
            ),
            Medicine(
                "000023",
                "약약",
                "더미덤",
                "더미23",
                "초코"
            ),
            Medicine(
                "000024",
                "약약",
                "더미덤",
                "더미24",
                "초코"
            ),
            Medicine(
                "000025",
                "약약",
                "더미덤",
                "더미25",
                "초코"
            ),
            Medicine(
                "000026",
                "약약",
                "더미덤",
                "더미26",
                "초코"
            ),
            Medicine(
                "000027",
                "약약",
                "더미덤",
                "더미27",
                "초코"
            ),
            Medicine(
                "000028",
                "약약",
                "더미덤",
                "더미28",
                "초코"
            ),
            Medicine(
                "000029",
                "약약",
                "더미덤",
                "더미29",
                "초코"
            ),
        )
    }
}