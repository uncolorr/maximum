package com.example.maximumhackathon.domain.engines

import com.example.maximumhackathon.domain.model.ResponseTranslate
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiTranslate {

    @GET("api/v1/dicservice.json/lookup")
    fun translate(
        @Query("key") key: String = "dict.1.1.20210331T152938Z.7944c9add65f96a7.d7190c23add2bcb3a3db3814dfcacc3b3e1148e4",
        @Query("lang") lang: String = "en-ru",
        @Query("text") text: String
    ): Single<ResponseTranslate>
}