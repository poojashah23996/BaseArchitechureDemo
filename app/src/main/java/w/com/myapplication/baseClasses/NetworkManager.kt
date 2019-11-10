package w.com.myapplication.baseClasses

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import w.com.myapplication.ResponseData
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkManager {
    val apiError = MutableLiveData<String>()
    val apiResponse: MutableLiveData<ResponseData<*>> = MutableLiveData()
    private lateinit var call: Call<*>
    val gson = Gson()

    fun <T> requestData(call: Call<T>, key: String) {
        this.call = call
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                val data = response as Response<ResponseData<T>>
                Log.d("NetworkManager", "onResponse$data")
                if (response.isSuccessful) {
                    data.body()?.key = key
                    apiResponse.postValue(data.body())
                } else {
                    val errorData =
                        gson.fromJson(response.errorBody()?.charStream(), ResponseData::class.java)
                    if (errorData != null) {
                        when {
                            data.code() == 401 -> apiError.postValue(SESSION_EXPIRE_MSG)
                            else -> call?.let { apiError.postValue(errorData.message) }
                        }
                    } else {
                        apiError.postValue(SERVER_ERROR)
                    }
                }

            }

            override fun onFailure(call: Call<T>, t: Throwable?) {
                Log.d("NetworkManager", "onError$t")
                if (call.isCanceled) {
                    Log.e("NetworkMaager", "request was cancelled")
                } else {

                    val message: String = when (t) {
                        is ConnectException -> NETWORK_ERROR
                        is UnknownHostException -> NETWORK_ERROR
                        is SocketTimeoutException -> "Please try again later…"
                        else -> SERVER_ERROR
                    }
                    apiError.postValue (message)
                }


            }
        })
    }
    //


    fun cancelCall() {
        if (!call.isCanceled) {
            call.cancel()
        }
    }
}