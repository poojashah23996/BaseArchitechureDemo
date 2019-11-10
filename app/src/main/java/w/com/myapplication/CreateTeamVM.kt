package w.com.myapplication

import retrofit2.Call
import w.com.myapplication.baseClasses.BaseViewModel
import w.com.myapplication.baseClasses.LIST_TEAM

class CreateTeamVM : BaseViewModel() {

    private var call4: Call<ResponseData<TeamList>>? = null
    fun listTeam() {
        setLoading(true)
        call4 = RetrofitClient.getApiInterface().listTeam()
        call4.let { it1 -> it1?.let { getNetworkManager().requestData(it, LIST_TEAM) } }
    }

    override fun onCleared() {
        super.onCleared()
        when {
            call4 != null -> call4.let {
                it?.cancel()
            }
        }
    }


}