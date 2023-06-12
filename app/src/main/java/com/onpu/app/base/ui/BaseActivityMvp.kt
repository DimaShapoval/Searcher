package com.onpu.app.base.ui

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.viewbinding.ViewBinding
import com.onpu.app.util.setupUI
import com.onpu.app.util.toast
import com.onpu.domain.mvp.base.ui.BaseMvp.BasePresenter
import com.onpu.domain.mvp.base.ui.BaseMvp.BaseView

abstract class BaseActivityMvp<P : BasePresenter<V>, V : BaseView, VB : ViewBinding> :
    BaseActivity<VB>(), BaseView {
    protected abstract val presenter: P
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)

        presenter.onCreate()
    }


    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        setupUI(parent)
        return super.onCreateView(parent, name, context, attrs)
    }

    override fun onBackPressed() = super.onBackPressed()

    override fun showErrorToast(message: String) = toast(message)


//    override fun addLogAboutSystemInfo() {
//        val pathname = filesDir.path + "/" + BuildConfig.LOG_FILE_NAME
//        val file = File(pathname).apply {
//            if (!this.exists()) this.createNewFile()
//        }
//        val dataAtFile = file.readFile()
//
//        val title = "Device and OS Parameters"
//        if (!dataAtFile.contains(title)) {
//            val model = Build.MODEL
//            val manufacturer = Build.MANUFACTURER
//            val osVersion = Build.VERSION.RELEASE
//            val apiLevel = Build.VERSION.SDK_INT
//
//            val logString = dataAtFile + "\n\t\t\t$title\n\n" +
//                    "\t model : " + model +
//                    "\t manufacturer : " + manufacturer +
//                    "\t osVersion : " + osVersion +
//                    "\t apiLevel : " + apiLevel + "\n\n"
//            file.writeText(logString, Charsets.UTF_8)
//        }
//    }

//    private fun addDataToLog(type: String, data: String) {
//        val currentDate = Date()
//        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
//        val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//        val dateText: String = dateFormat.format(currentDate)
//        val timeText: String = timeFormat.format(currentDate)
//        val pathname = filesDir.path + "/" + BuildConfig.LOG_FILE_NAME
//        val file = File(pathname).apply {
//            if (!this.exists()) this.createNewFile()
//        }
//        val logString = file.readFile() + "<--[$type] DATE/TIME : ($dateText/$timeText) \n" +
//                "\t|>  data : $data  \n\n"
//
//        file.writeText(logString, Charsets.UTF_8)
//    }


    override fun onStart() {
        presenter.onStart()
        super.onStart()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onRestart() {
        super.onRestart()
        presenter.onRestart()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

}