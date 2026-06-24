package com.serify.data.api

import com.google.gson.JsonElement
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleTranslateApi {

    @GET("translate_a/single")
    suspend fun translateText(
        @Query("client") client: String = "gtx",
        @Query("sl") sourceLanguage: String = "en",
        @Query("tl") targetLanguage: String = "es",
        @Query("dt") dataType: String = "t",
        @Query("q") text: String
    ): JsonElement
}
