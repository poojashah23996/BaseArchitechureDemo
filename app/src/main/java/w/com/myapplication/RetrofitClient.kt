package w.com.myapplication

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import w.com.myapplication.baseClasses.ApiService
import w.com.myapplication.baseClasses.App
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val httpClient = OkHttpClient.Builder()
//    private val loggingInterceptor =
//        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val headerInterceptor: HeaderInterceptor = HeaderInterceptor()
    private var apiService: ApiService


    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        httpClient.connectTimeout(25, TimeUnit.SECONDS)
        httpClient.readTimeout(25, TimeUnit.SECONDS)
        httpClient.retryOnConnectionFailure(true)
        addInterceptors()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
        apiService = retrofit.create(ApiService::class.java)

    }


    fun getApiInterface(): ApiService {
        return apiService
    }

    private fun addInterceptors() {
        if (!httpClient.interceptors().contains(headerInterceptor)) {
            httpClient.addInterceptor(headerInterceptor)
        }

//        if (BuildConfig.DEBUG && !httpClient.networkInterceptors().contains(loggingInterceptor)) {
//            httpClient.addNetworkInterceptor(loggingInterceptor)
//        }
        httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {

                val request = chain.request()
                val response = chain.proceed(request)

                // todo deal with the issues the way you need to
                if (response.code() == 402) {
                    App.adiErrorUpdateGlobal.onNext(response.message())
                    return response
                }

                return response
            }

        })


    }

}