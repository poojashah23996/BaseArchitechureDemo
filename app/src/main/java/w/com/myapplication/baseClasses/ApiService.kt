package w.com.myapplication.baseClasses

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import w.com.myapplication.ResponseData
import w.com.myapplication.TeamList

const val LIST_TEAM = "api/team/listTeam"
interface ApiService {

    @POST(LIST_TEAM)
    fun listTeam(): Call<ResponseData<TeamList>>


}
