package com.blackcows.butakaeyak.ui.search.data

import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.blackcows.butakaeyak.data.models.Medicine
import com.google.gson.Gson

private const val TAG = "약 데이터"
class DataSource {
    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSoures(): DataSource {
            // DatoSource::class 객체에 lock을 걸어 한번에 한 스레드에서만 실행 되도록 함
            return synchronized(DataSource::class) {
                // 싱글톤 객체를 한번 호출하고 없으면 DataSource반환, 있으면 생성된 인스턴스 반환
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }

        //SharedPreferences
        fun saveData(context: android.content.Context,fileName: String,key: String,data: String) {
            context.getSharedPreferences(fileName, MODE_PRIVATE)
                .edit()
                .putString(key, data)
                .apply() // 저장완료
        }
        fun satItemChecked(context: android.content.Context,fileName: String,key: String,isChecked: Boolean) {
            context.getSharedPreferences(fileName, MODE_PRIVATE).edit().putBoolean(key, isChecked).apply() // 저장완료
            Log.d(TAG, key +" str "+ context.getSharedPreferences(fileName, MODE_PRIVATE).getBoolean(key,false).toString())
        }

        fun isItemChecked(context: android.content.Context, fileName: String,key: String) : Boolean {
            Log.d(TAG, key +" - "+ context.getSharedPreferences(fileName, MODE_PRIVATE).getBoolean(key,false))
            return context.getSharedPreferences(fileName, MODE_PRIVATE).getBoolean(key,false)
        }
        fun loadAllData(context: android.content.Context, fileName: String) : Medicine {
            val pref = context.getSharedPreferences(fileName, MODE_PRIVATE).all.toString()
            Log.d(TAG,context.getSharedPreferences(fileName, MODE_PRIVATE).all.toString())
            return parseJson(pref)
        }
        fun parseJson(jsonString: String): Medicine {
            val gson = Gson()
            return gson.fromJson(jsonString, Medicine::class.java)
        }
    }

    // MVVM패턴에서 Model에 해당한다고 볼 수 있음
    fun getMedicineList(): List<Medicine> {
        // 만들어놓은 데이터클래스 리턴
        return MedicineDataList()
    }

    fun MedicineDataList(): ArrayList<Medicine> {
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