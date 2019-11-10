package w.com.myapplication.baseClasses

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import w.com.myapplication.ResponseData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.Disposable
import w.com.myapplication.R

abstract class BaseActivity : AppCompatActivity(), ActImpMethods {
    private var binding: ViewDataBinding? = null
    lateinit var baseViewModel: BaseViewModel
    private lateinit var progressDialog: Dialog
    protected fun setView(layoutResId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutResId)
        try {
            initVariable()
            loadData()
        } catch (e: Exception) {
            e.printStackTrace()
            //  GeneralUtils.showToast(BaseActivity.this, e.toString());
            Log.e(TAG, e.toString())
        }
    }


    inline fun <reified T : BaseViewModel> AppCompatActivity.getViewModel(): BaseViewModel {

        baseViewModel = ViewModelProviders.of(this)[T::class.java]
        setObserve()
        return baseViewModel as T
    }

    open fun apiResponseError(it: ResponseData<*>?) {
    }

    fun setObserve() {
        baseViewModel.loading.observe(this, Observer { loading ->
            if (loading) showProgressDialog() else hideProgressDialog()
        })

        baseViewModel.apiResponse.observe(this, Observer {
            hideProgressDialog()
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
                Log.e("BaseActivity", "apiError basActivity$error")
                if (error == SESSION_EXPIRE_MSG) {
                    Log.d("BAseDialog", "preference clear")
                    Log.e("BaseActivity", "Session expires")
                    PreferenceHelper.getInstance().clearPrefs()
                    // startActivity(WelcomeActivity.newIntent(this))
                    // finishAffinity()
                }
            }

        })

    }

    open fun apiError(error: String) {
        Log.e("BaseAct", "apiError")
        // when error call this method use in Activity
    }

    open fun apiResponse(response: ResponseData<*>) {
        Log.e("BaseActivity", "apiResponse")
        // when response call this method use in Activity
    }


    fun messageWarning(stringResId: Int) {
        Snackbar.make(binding!!.root, stringResId, 1500).show()
    }

    fun messageWarning(stringMsg: String) {
        Snackbar.make(binding!!.root, stringMsg, 1500).show()
    }

    protected fun <T : ViewDataBinding> getBinding(): T? {
        return binding as T?
    }

    fun addFragment(container: Int, fragment: BaseFragment, backStackTag: String) {
        // hideKeyboard(this);
        if (backStackTag.isEmpty()) {
            supportFragmentManager.beginTransaction()
                .add(container, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().addToBackStack(backStackTag)
                .add(container, fragment, fragment.javaClass.simpleName).commit()
        }
    }


    fun changeFragment(container: Int, fragment: BaseFragment, backStackTag: String = "") {
        if (backStackTag.isEmpty()) {
            supportFragmentManager.beginTransaction()
                .replace(container, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().addToBackStack(backStackTag)
                .replace(container, fragment, fragment.javaClass.simpleName).commit()
        }

    }


    fun isConnected(mContext: Context): Boolean {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // assert cm != null;
        var activeNetwork: NetworkInfo? = null
        activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun showProgressDialog(isCancel: Boolean = false) {
        if (!::progressDialog.isInitialized) {
            progressDialog = Dialog(this, R.style.ProgressDialog)
            progressDialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.dialog_progressbar)
                val layoutParams = window?.attributes
                layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
                setCanceledOnTouchOutside(false)
                setCancelable(false)
                window?.attributes = layoutParams
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

        }
        if (::progressDialog.isInitialized && !progressDialog.isShowing) {
            progressDialog.show()
        }

    }

    public fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    /**
     * @param layout      Layout ID
     * @param fragment    Fragment
     * @param isBaskStack Boolean True or false_img
     * @param TAG         Fragment Tag
     */
    fun changeFragment(layout: Int, fragment: Fragment, isBaskStack: Boolean, TAG: String) {
        //hide keyboard when fragment change
        hideKeyboard(this)
        if (!isBaskStack) {
            supportFragmentManager.beginTransaction().replace(layout, fragment, TAG).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(layout, fragment, TAG)
                .addToBackStack(null).commit()
        }
    }
  override fun onStop() {
        hideProgressDialog()
        super.onStop()
    }

    @CallSuper
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }


    companion object {

        private val TAG = BaseActivity::class.java.simpleName


        fun hideKeyboard(activity: Activity) {

            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    lateinit var disposable: Disposable

    override fun onResume() {
        super.onResume()
    }


    override fun onPause() {
        if (::disposable.isInitialized && !disposable.isDisposed)
            disposable.dispose()
        super.onPause()

    }

}
