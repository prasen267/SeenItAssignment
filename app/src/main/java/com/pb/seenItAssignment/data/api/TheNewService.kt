package com.pb.seenItAssignment.data.api

import com.pb.seenItAssignment.data.model.NewsResponse
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface TheNewService {

    @GET("v2/top-headlines")
    fun getTopHeadLines(@Query("page") page: Int): Single<NewsResponse>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): TheNewService {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("apiKey", API_KEY)
                    .addQueryParameter("country", COUNTRY)
                    .addQueryParameter("pageSize", NEWS_PER_PAGE)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val okhttpbuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)

                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.SECONDS)
            val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okhttpbuilder.addInterceptor(logging)



            val retrofit = Retrofit.Builder()
                .client(okhttpbuilder.build())
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(TheNewService::class.java)
            return service

        }
    }
}