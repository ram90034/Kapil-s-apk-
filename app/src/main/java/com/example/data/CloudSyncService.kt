package com.example.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.security.MessageDigest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class SyncPayload(
    val email: String,
    val name: String,
    val workouts: List<WorkoutLog>,
    val weights: List<BodyWeightLog>,
    val timestamp: Long = System.currentTimeMillis()
)

object CloudSyncService {
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        
    private const val BUCKET_NAME = "oldskoolfitness_sync"
    
    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return md.digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
    
    suspend fun backupToCloud(payload: SyncPayload): Boolean = withContext(Dispatchers.IO) {
        val userHash = md5(payload.email.trim().lowercase())
        val url = "https://kvdb.io/$BUCKET_NAME/$userHash"
        
        val adapter = moshi.adapter(SyncPayload::class.java)
        val jsonString = adapter.toJson(payload)
        
        val body = jsonString.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .put(body) // Put updates or creates the key in kvdb.io
            .build()
            
        try {
            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    suspend fun restoreFromCloud(email: String): SyncPayload? = withContext(Dispatchers.IO) {
        val userHash = md5(email.trim().lowercase())
        val url = "https://kvdb.io/$BUCKET_NAME/$userHash"
        
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
            
        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val bodyString = response.body?.string() ?: return@withContext null
                    val adapter = moshi.adapter(SyncPayload::class.java)
                    adapter.fromJson(bodyString)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
