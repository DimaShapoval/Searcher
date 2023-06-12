package com.onpu.app.base.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.viewbinding.ViewBinding
import com.onpu.app.databinding.FragmentBaseBinding
import com.onpu.app.util.toast
import com.onpu.domain.mvp.base.ui.BaseMvp.BasePresenter
import com.onpu.domain.mvp.base.ui.BaseMvp.BaseView

abstract class BaseFragmentMvp<P : BasePresenter<V>, V : BaseView, VB : ViewBinding> :
    BaseFragment<VB>(), BaseView {
    abstract val presenter: P

    lateinit var requestLocationPermission: ActivityResultLauncher<Array</*out */String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val baseBinding = FragmentBaseBinding.inflate(inflater, container, false)
        val v = super.onCreateView(inflater, baseBinding.root, savedInstanceState)
        baseBinding.root.addView(v, 0)
        presenter.attachView(this as V)
        return baseBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun showErrorToast(message: String) = toast(message)

    override fun onBackPressed() = requireActivity().onBackPressed()


//    private fun showRequestPermissionDialog() {
//        val dialogHelper = PermissionRequestDialogHelper(requireContext())
//        dialogHelper.showRequestLocationDialog { _, _ ->
//            if(!shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
//                dialogHelper.showRetryRequestLocationDialog { _, _ ->
//                    launchAppSettings()
//                }
//            } else {
//                requestLocationPermission.launch(arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION))
//            }
//        }
//    }

    private fun launchAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", requireActivity().packageName, null)
        startActivity(intent)
    }

//    override fun addLogAboutSuccess(text: String) {
//        addDataToLog("SUCCESS REQUEST", text)
//    }
//
//    override fun addLogAboutError(e: Throwable) {
//        if (e is ServerError) {
//            addDataToLog("ERROR REQUEST", e.toString())
//        } else addDataToLog("ERROR REQUEST", e.stackTraceToString())
//    }

//    override fun addLogAboutSystemInfo() {
//        val pathname = requireContext().filesDir.path + "/" + BuildConfig.LOG_FILE_NAME
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
//        val pathname = requireContext().filesDir.path + "/" + BuildConfig.LOG_FILE_NAME
//        val file = File(pathname).apply {
//            if (!this.exists()) this.createNewFile()
//        }
//        val logString = file.readFile() + "<--[$type] DATE/TIME : ($dateText/$timeText) \n" +
//                "\t|>  data : $data  \n\n"
//
//        file.writeText(logString, Charsets.UTF_8)
//    }

}