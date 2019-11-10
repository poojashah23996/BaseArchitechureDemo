package w.com.myapplication

import com.google.gson.annotations.SerializedName

data class ResponseData<T>(
    var message: String? = null,
    var data: T? = null,
    var status: Int? = null,
    var key: String? = null

)

data class TeamList(
    @SerializedName("list")
    var teamList: List<Team>? = null
)

data class Team(
    var teamId: Int,
    var teamImage: String? = null,
    var myProfileImage: String? = null,
    var userId: Int,
    var firstName: String? = null,
    var lastName: String? = null,
    var profileImage: String? = null,
    var status: String? = null,
    var isPrimaryUser: Boolean=false
)
