package w.com.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import w.com.myapplication.baseClasses.BaseViewHolder
import w.com.myapplication.databinding.RowTeamBinding

class TeamAdapter(
    private val teamList: MutableList<Team?>,
    private val mClick: RecycleItemClick?,
    private var teamId: Int
) :
    RecyclerView.Adapter<TeamAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.e(
            "TeamAdaoter", "teamId$teamId"
        )
        holder.mBinding?.model = teamList[position]
        if (teamList[position]?.teamId == teamId) {
            holder.mBinding?.active?.visibility = View.VISIBLE
        } else {
            holder.mBinding?.active?.visibility = View.GONE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DataBindingUtil.inflate<RowTeamBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_team,
            parent,
            false
        )
        return MyViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return teamList.size
    }

    inner class MyViewHolder(itemBinding: RowTeamBinding) :
        BaseViewHolder(itemBinding.teamLl) {

        var mBinding: RowTeamBinding? = null

        init {
            this.mBinding = itemBinding
            mBinding?.editBtn?.setOnClickListener {
                mClick?.onItemClick(adapterPosition, teamList[adapterPosition]?.teamId, 0)
            }
            mBinding?.teamLl?.setOnClickListener {
                mClick?.onItemClick(adapterPosition, teamList[adapterPosition]?.teamId, 1)
            }

        }

    }
}