package com.onpu.domain.mvp.sample

import com.onpu.domain.data.error.BaseResponseError
import com.onpu.domain.data.error.ServerError
import com.onpu.domain.mvp.adapter.SampleAdapterMvp
import com.onpu.domain.mvp.base.ui.BasePresenterImpl
import com.onpu.domain.util.use

class SamplePresenter : BasePresenterImpl<SampleMvp.View>(), SampleMvp.Presenter, SampleAdapterMvp.Presenter {

    override fun onCreate() {
        view?.initListeners()
        view?.onAdapterCreated(this)
    }

    override var adapter: SampleAdapterMvp.Adapter? = null

    override fun onAdapterCreated() = adapter.use {
        val item = BaseResponseError("", ServerError.Code.ERROR1)
        items.clear()
        repeat(5) { items.add(item) }
        notifyDataSetChanged()
        adapter?.stringFreeServers = "some Text"
    }

    override fun onTitleClick(item: BaseResponseError) {
    }

    override fun onFindTextChanged(findText: String) {
        val cache = adapter?.items
        val position = 0
        cache?.removeAt(position)
        adapter?.items?.clear()
        adapter?.items?.addAll(cache?.toList().orEmpty())
        adapter?.notifyItemRemoved(position)
        if(findText.isEmpty()){
            adapter?.items?.clear()
            adapter?.notifyDataSetChanged()
        }
    }

}