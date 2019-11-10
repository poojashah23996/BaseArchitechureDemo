package w.com.myapplication.baseClasses

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import io.reactivex.subjects.PublishSubject

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.initialize(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        instance = this
        mPref = PreferenceHelper.getInstance()
    }

    companion object {
        val  adiErrorUpdateGlobal = PublishSubject.create<String>()

        var instance: App? = null
            protected set
        var mPref: PreferenceHelper? = null
    }

}
