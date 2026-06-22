package com.serify.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.serify.data.model.Serie
import com.serify.data.util.SpanishTextMapper
import kotlinx.coroutines.tasks.await

class FirebaseSeriesRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun currentUserId(): String? {
        return auth.currentUser?.uid
    }

    private fun savedSeriesCollection(userId: String) =
        db.collection("users")
            .document(userId)
            .collection("savedSeries")

    suspend fun saveSerie(serie: Serie) {
        val userId = currentUserId() ?: return

        val data = hashMapOf(
            "id" to serie.id,
            "name" to serie.name,
            "summary" to serie.summary,
            "imageUrl" to serie.imageUrl,
            "rating" to serie.rating,
            "genres" to serie.genres,
            "premiered" to serie.premiered,
            "status" to serie.status,
            "savedAt" to FieldValue.serverTimestamp()
        )

        savedSeriesCollection(userId)
            .document(serie.id.toString())
            .set(data)
            .await()
    }

    suspend fun removeSerie(serieId: Int) {
        val userId = currentUserId() ?: return

        savedSeriesCollection(userId)
            .document(serieId.toString())
            .delete()
            .await()
    }

    suspend fun isSerieSaved(serieId: Int): Boolean {
        val userId = currentUserId() ?: return false

        val document = savedSeriesCollection(userId)
            .document(serieId.toString())
            .get()
            .await()

        return document.exists()
    }

    suspend fun toggleSavedSerie(serie: Serie): Boolean {
        val isSaved = isSerieSaved(serie.id)

        return if (isSaved) {
            removeSerie(serie.id)
            false
        } else {
            saveSerie(serie)
            true
        }
    }

    suspend fun getSavedSerieIds(): Set<Int> {
        val userId = currentUserId() ?: return emptySet()

        val snapshot = savedSeriesCollection(userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.getLong("id")?.toInt()
        }.toSet()
    }

    suspend fun getSavedSeries(): List<Serie> {
        val userId = currentUserId() ?: return emptyList()

        val snapshot = savedSeriesCollection(userId)
            .orderBy("savedAt", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            val id = document.getLong("id")?.toInt() ?: return@mapNotNull null
            val name = document.getString("name") ?: return@mapNotNull null

            @Suppress("UNCHECKED_CAST")
            val genres = document.get("genres") as? List<String> ?: emptyList()

            Serie(
                id = id,
                name = name,
                summary = document.getString("summary"),
                imageUrl = document.getString("imageUrl"),
                rating = document.getDouble("rating"),
                genres = genres.map { genre -> SpanishTextMapper.genre(genre) },
                premiered = document.getString("premiered"),
                status = SpanishTextMapper.status(document.getString("status"))
            )
        }
    }

    suspend fun getSavedCount(): Int {
        val userId = currentUserId() ?: return 0

        return savedSeriesCollection(userId)
            .get()
            .await()
            .size()
    }
}
