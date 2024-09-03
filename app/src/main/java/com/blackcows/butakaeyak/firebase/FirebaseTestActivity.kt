package com.blackcows.butakaeyak.firebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blackcows.butakaeyak.databinding.FirebaseSampleLayoutBinding
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseTestActivity : AppCompatActivity() {
    private var _binding: FirebaseSampleLayoutBinding? = null
    private val binding get() = _binding!!

    private var sampleNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FirebaseSampleLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDocument(
            FirebaseData(
                sampleName = "firstData",
                sampleNumber = sampleNumber,
                sampleBoolean = false
            )
        )

        binding.buttonSet.setOnClickListener {
            sampleNumber++
            setDocument(
                FirebaseData(
                    sampleName = "sampleData$sampleNumber",
                    sampleNumber = sampleNumber,
                    sampleBoolean = true
                )
            )
        }
    }

    private fun setDocument(data: FirebaseData) {
        FirebaseFirestore.getInstance()
            .collection("sampleCollection")
            .document(data.sampleName)
            .set(data)
            .addOnSuccessListener {
                binding.textResult.text = "success!"
            }
            .addOnFailureListener {
                binding.textResult.text = "fail!"
            }
    }
}
