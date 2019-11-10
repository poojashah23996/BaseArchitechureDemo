package w.com.myapplication.baseClasses

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import w.com.myapplication.ResponseData


abstract class BaseFragment : Fragment(), ActImpMethods {
    private var hasOptionMenu = false
    lateinit var baseViewModel: BaseViewModel
    private var layoutId: Int = 0

    protected var activity: BaseActivity? = null
    private var binding: ViewDataBinding? = null
    private var dialog: Dialog? = null

    protected fun setView(layoutId: Int) {
        this.layoutId = layoutId
        hasOptionMenu = false
    }

    protected fun setView(layoutId: Int, hasOptionMenu: Boolean) {
        this.layoutId = layoutId
        this.hasOptionMenu = hasOptionMenu
    }

    inline fun <reified T : BaseViewModel> AppCompatActivity.getViewModel(): BaseViewModel {
        baseViewModel = activity?.let { ViewModelProviders.of(it) }!![T::class.java]
        setObserve()
        return baseViewModel as T
    }

    inline fun <reified T : BaseViewModel> AppCompatActivity.getViewModel(activity: Fragment): BaseViewModel {
        baseViewModel = ViewModelProviders.of(activity) [T::class.java]
        setObserve()
        return baseViewModel as T
    }


    open fun apiError(error: String) {
        Log.e("BaseFragment","apiError")
        // when error call this method use in Activity
    }

    open fun apiResponse(response: ResponseData<*>) {
        Log.e("BaseFragment","apiResponse")
        // when response call this method use in Activity
    }

    open fun apiResponseError(it: ResponseData<*>?) {
    }

    fun setObserve() {
        baseViewModel.loading.observe(this, Observer { loading ->
            if (loading) activity?.showProgressDialog() else activity?.hideProgressDialog()
        })

        baseViewModel.apiResponse.observe(this, Observer {
            activity?.hideProgressDialog()
            if (it.status == FAILURE) {
                apiResponseError(it)
            } else {
                apiResponse(it)

            }

        })

        baseViewModel.apiError.observe(this, Observer { error ->
            baseViewModel.setLoading(false)
            apiError(error)
            if (!TextUtils.isEmpty(error)) {
                Log.e("BaseFragment","apiError basActivity")
                if (error == SESSION_EXPIRE_MSG) {
                    PreferenceHelper.getInstance().clearPrefs()
//                    startActivity(activity?.let { WelcomeActivity.newIntent(it) })
//                    activity?.finishAffinity()
                }
            }

        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        if (hasOptionMenu) {
            setHasOptionsMenu(true)
        }
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity() as BaseActivity
    }

    fun messageWarning(msg: Int) {
        activity!!.messageWarning(msg)
    }


    fun messageWarning(stringMsg: String) {
        activity!!.messageWarning(stringMsg)
    }

    fun messageWarning(stringMsg: String, timeInterval: Int) {
        activity!!.messageWarning(stringMsg)
    }

    protected fun <T : ViewDataBinding> getBinding(): T? {
        return binding as T?
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {
            initVariable()
            loadData()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show()
            Log.e(TAG, e.toString())
        }


    }
    companion object {

        private val TAG = BaseFragment::class.java.simpleName
    }


}
