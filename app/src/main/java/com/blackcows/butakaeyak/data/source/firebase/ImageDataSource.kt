package com.blackcows.butakaeyak.data.source.firebase

import android.graphics.Bitmap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageDataSource @Inject constructor(

) {
    companion object {
        private const val TAG = "ImageDataSource"

        private const val ROOT = "gs://yaktong-6651a.appspot.com/"
        private const val PATH = "profile"
    }

    private val storage = Firebase.storage

    suspend fun uploadProfile(userId: String, bitmap: Bitmap) {
        val storageRef = storage.reference.child(PATH).child(userId)

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val data = stream.toByteArray()
        storageRef.putBytes(data).await()
    }

    suspend fun getHttpUrl(userId: String): String {
        val gsUrl = "$ROOT$PATH/$userId"
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(gsUrl)


        val httpUrl = storageReference.downloadUrl.await()

        return httpUrl.toString()
    }

    suspend fun deleteProfile(userId: String) {
        storage.reference.child(PATH).child(userId)
            .delete().await()
    }
}