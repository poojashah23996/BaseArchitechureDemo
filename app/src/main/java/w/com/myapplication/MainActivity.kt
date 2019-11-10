package w.com.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import w.com.myapplication.baseClasses.BaseActivity
import w.com.myapplication.baseClasses.LIST_TEAM
import w.com.myapplication.databinding.ActivityMainBinding

class MainActivity : BaseActivity(), RecycleItemClick {
    override fun onItemClick(position: Int, o: Any?, type: Int) {

    }

    private val createTeamVM: CreateTeamVM by lazy {
        getViewModel<CreateTeamVM>() as CreateTeamVM
    }
    private var mBinding: ActivityMainBinding? = null
    override fun initVariable() {
        mBinding = getBinding()
        createTeamVM.listTeam()

    }

    override fun loadData() {
        mBinding?.swipeContainer?.setOnRefreshListener {
            teams.clear()
            mBinding?.swipeContainer?.isRefreshing = false
            createTeamVM.listTeam()
        }
        mBinding?.swipeRefreshLayoutEmptyView?.setOnRefreshListener {
            teams.clear()
            mBinding?.swipeRefreshLayoutEmptyView?.isRefreshing = false
            createTeamVM.listTeam()
        }
        mBinding?.rvTeam?.layoutManager = LinearLayoutManager(this)
        mBinding?.rvTeam?.adapter = teamAdapter
    }

    private val teamAdapter: TeamAdapter by lazy {
        TeamAdapter(teams, this, teamId)
    }
    private var teamId = 0

    override fun apiError(error: String) {
        super.apiError(error)
        messageWarning(getString(R.string.something_went_wrong))

    }

    private val teams: MutableList<Team?> = mutableListOf()


    @SuppressLint("SetTextI18n")
    override fun apiResponse(response: ResponseData<*>) {
        Log.e("MessageFrag", "apiresponse")
        super.apiResponse(response)
        if (response.key == LIST_TEAM) {
            if (response.status == 1) {
                val teamList = response.data as TeamList
                val team = teamList.teamList as ArrayList<Team>
                teams.clear()
                for (i in 0 until team.size) {
                    val model = team[i]
                    teams.add(model)
                }
                if (teams.isEmpty()) {
                    mBinding?.swipeContainer?.visibility = View.GONE
                    mBinding?.swipeRefreshLayoutEmptyView?.visibility = View.VISIBLE
                } else {
                    mBinding?.swipeContainer?.visibility = View.VISIBLE
                    mBinding?.swipeRefreshLayoutEmptyView?.visibility = View.GONE
                    teamAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView(R.layout.activity_main)
    }
}
