package com.onpu.app.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import com.onpu.app.base.ui.BaseActivityMvp
import com.onpu.app.databinding.ActivityMainBinding
import com.onpu.app.util.*
import com.onpu.domain.model.MathOperation
import com.onpu.domain.model.SpecifiersEnums
import com.onpu.domain.model.StringSearch
import com.onpu.domain.mvp.ui.main_screen.MainMvp
import com.onpu.domain.mvp.ui.main_screen.MainPresenter
import com.onpu.domain.util.toString
import com.onpu.domain.util.use
import java.lang.StringBuilder

class MainActivity : BaseActivityMvp<MainMvp.Presenter, MainMvp.View, ActivityMainBinding>(),
    MainMvp.View {

    override val presenter: MainMvp.Presenter by lazy { MainPresenter() }

    override fun inflate(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.use {
            myWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            myWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    requestField.setText(url); return false
                }
            }

            requestField.requestFocus()
            requestField.onDoneOrNextClickListener {
                presenter.searchSiteByLink(requestField.text.toString())
            }
        }
    }

    override fun showSavedLink(link: String): Unit? = binding?.requestField?.setText(link)

    override fun showError() = binding.use {
        progressBar.isVisible = false
        toast("Invalid link")
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun generalSearch(url: String) = binding.use {
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl(url)
    }

    override fun showNavigationView(text: String) = binding.use {
        var selectedSpecifierIndex = 0
        val paramsWithSpecifier =
            text.getAllParamsFromLink().filter { it.value.contains(SpecifiersEnums.INT.name) }
        val currentSelectedParameter = paramsWithSpecifier[selectedSpecifierIndex]

        nextValue.isVisible = paramsWithSpecifier.size > 1
        plusValue.isVisible = true
        minusValue.isVisible = true
//        prevValue.isVisible = true

        showSelectedSpecifier(currentSelectedParameter)
        minusValue.onClickListener { editIntValueAndShowResult(text, currentSelectedParameter, MathOperation.SUBTRACTION) }
        plusValue.onClickListener { editIntValueAndShowResult(text, currentSelectedParameter, MathOperation.ADDITION) }
    }

    private fun editIntValueAndShowResult(link : String, currentSelectedParameter: StringSearch, operation: MathOperation) {
        var builder = StringBuilder(link)
        println("dawdawihj " + builder.toString())
        builder = StringBuilder(builder.removeRange(IntRange(currentSelectedParameter.indexStartText, currentSelectedParameter.indexEndText - 1)))
        currentSelectedParameter.specifierValue = when (operation) {
            MathOperation.ADDITION -> currentSelectedParameter.specifierValue + 1
            MathOperation.SUBTRACTION -> currentSelectedParameter.specifierValue - 1
            else -> 0
        }
        builder.insert(currentSelectedParameter.indexStartText, (currentSelectedParameter.specifierValue).toString)
        toast(builder.toString())
        generalSearch(builder.toString())
    }


    private fun showSelectedSpecifier(currentSelectedParameter: StringSearch) = binding.use {
        requestField.requestFocus()
        requestField.setSelection(
            currentSelectedParameter.indexStartText,
            currentSelectedParameter.indexEndText
        )
    }

    private fun String.getAllParamsFromLink(): List<StringSearch> {
        val res = mutableListOf<StringSearch>()
        this.split("?")[1].split("&").forEach {
            val key = it.split("=")[0]
            val value = it.split("=")[1]
            res.add(
                StringSearch(
                    key,
                    value,
                    this.indexOf(value),
                    this.indexOf(value) + value.length
                )
            )
        }
        println(res)

        return res
    }

    override fun hideNavigationView() = binding.use {
        nextValue.isVisible = false
        plusValue.isVisible = false
        minusValue.isVisible = false
        prevValue.isVisible = false
    }
}