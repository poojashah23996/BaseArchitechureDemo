package w.com.myapplication.baseClasses

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var binding: ViewDataBinding? = null

    init {
        setBinding(itemView)
    }

    fun <T : ViewDataBinding> getBinding(): T? {
        return binding as T?
    }

    private fun setBinding(itemView: View) {
        binding = DataBindingUtil.bind(itemView)
    }
}