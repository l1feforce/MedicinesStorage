package ru.spbstu.gusev.medicinesstorage.data.network.grls

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.spbstu.gusev.medicinesstorage.BuildConfig

const val MEDICINES_DATABASE_URL = "https://medicines-storage.herokuapp.com/"
const val MEDICINES_DATABASE_URL_DEBUG = "http://192.168.0.180:5000/"

fun provideHttpInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply {
        setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

fun provideMedicinesDatabaseHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()


fun provideMedicinesDatabaseRetrofit(httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(MEDICINES_DATABASE_URL)
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
        .client(httpClient)
        .build()
}

fun provideMedicinesDatabaseApi(retrofit: Retrofit): GrlsServiceApi = retrofit.create(GrlsServiceApi::class.java)