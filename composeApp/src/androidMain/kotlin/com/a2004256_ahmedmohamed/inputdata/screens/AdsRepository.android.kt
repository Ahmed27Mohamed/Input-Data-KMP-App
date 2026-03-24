package com.a2004256_ahmedmohamed.inputdata.screens

import com.a2004256_ahmedmohamed.inputdata.models.AdsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

actual class AdsRepository {

        private val database by lazy { FirebaseDatabase.getInstance() }

        actual suspend fun getAdsByUser(uid: String): List<AdsModel> {
            return try {
                val snapshot = database.getReference("users")
                    .child(uid)
                    .child("Ads")
                    .get()
                    .await()

                val adsList = mutableListOf<AdsModel>()
                for (adSnapshot in snapshot.children) {
                    val ad = adSnapshot.getValue(AdsModel::class.java)
                    if (ad != null) {
                        ad.id = adSnapshot.key ?: ""
                        ad.uid = uid
                        adsList.add(ad)
                    }
                }
                adsList
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

        actual fun observeAdsByUser(uid: String): Flow<List<AdsModel>> = callbackFlow {
            val adsRef = database.getReference("users").child(uid).child("Ads")

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val adsList = mutableListOf<AdsModel>()
                    for (adSnapshot in snapshot.children) {
                        val ad = adSnapshot.getValue(AdsModel::class.java)
                        if (ad != null) {
                            ad.id = adSnapshot.key ?: ""
                            ad.uid = uid
                            adsList.add(ad)
                        }
                    }
                    trySend(adsList)
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

            adsRef.addValueEventListener(listener)

            awaitClose {
                adsRef.removeEventListener(listener)
            }
        }

        actual suspend fun deleteAd(uid: String, adId: String): Boolean {
            return try {
                database.getReference("users")
                    .child(uid)
                    .child("Ads")
                    .child(adId)
                    .removeValue()
                    .await()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

}