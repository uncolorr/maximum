package com.example.maximumhackathon.domain.engines

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class YandexEngine {

    private var api: ApiTranslate

    init {
        api = getRetrofit().create(ApiTranslate::class.java)
    }

    fun translate(word: String) = api.translate(text = word)

    private fun getRetrofit(): Retrofit {
        return getRetrofitBuilder().client(getOkHttpBuilder().build()).build()
    }

    private fun getRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://dictionary.yandex.net/")
    }

    private fun getOkHttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(getLoggerInterceptor())
        }
    }

    private fun getLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(
            logger = object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i("OkHttp", message)
                }
            }
        ).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun getGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }
}