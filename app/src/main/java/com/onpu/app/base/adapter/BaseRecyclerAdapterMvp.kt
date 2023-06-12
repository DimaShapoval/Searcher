package com.onpu.app.base.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.onpu.domain.mvp.base.adapter.BaseAdapterMvp.BaseAdapter
import com.onpu.domain.mvp.base.adapter.BaseAdapterMvp.BasePresenter

abstract class BaseRecyclerAdapterMvp<P : BasePresenter<A>, A : BaseAdapter, VB: ViewBinding>(
    protected val presenter: P
) : BaseRecyclerAdapter<VB>(), BaseAdapter {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        presenter.adapter = this as A
        presenter.onAdapterCreated()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        presenter.adapter = null
        super.onDetachedFromRecyclerView(recyclerView)
    }
}