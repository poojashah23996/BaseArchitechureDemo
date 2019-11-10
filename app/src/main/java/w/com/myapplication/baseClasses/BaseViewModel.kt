package w.com.myapplication.baseClasses

import android.util.Log
import androidx.lifecycle.*
import w.com.myapplication.ResponseData

open class BaseViewModel : ViewModel() {

    lateinit var responseDataObserver: Observer<ResponseData<*>>
    private var isLoading = MutableLiveData<Boolean>()
    private val networkManager: NetworkManager = NetworkManager()

    init {
        setObservable()
    }

    internal val loading: LiveData<Boolean>
        get() = isLoading


    internal val apiResponse: LiveData<ResponseData<*>>
        get() = this.networkManager.apiResponse


    internal val apiError: LiveData<String>
        get() = this.networkManager.apiError

    fun getNetworkManager(): NetworkManager = networkManager


    private fun setObservable() {
        // removeObserve() in BaseActivity
        responseDataObserver = Observer { responseData ->
            if (responseData.status != FAILURE) {
                apiResponse(responseData)
            }else{
                Log.e("BaseViewModel","response data code failure")
            }

        }
        apiResponse.observeForever(responseDataObserver)
    }

    fun setLoading(loading: Boolean) {
        isLoading.postValue(loading)
    }

    open fun apiResponse(responseData: ResponseData<*>) {
        setLoading(false)
    }
}