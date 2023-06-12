package com.onpu.domain.mvp.base.adapter

interface BaseAdapterMvp {

    interface BaseAdapter {
        fun notifyDataSetChanged()
        fun notifyItemChanged(position: Int)
        fun notifyItemRangeChanged(positionStart: Int, itemCount: Int)
        fun notifyItemInserted(position: Int)
        fun notifyItemRangeInserted(positionStart: Int, itemCount: Int)
        fun notifyItemRemoved(position: Int)
        fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int)
        fun notifyItemMoved(fromPosition: Int, toPosition: Int)
    }

    interface BasePresenter<A : BaseAdapter> {
        var adapter: A?
//        fun attachAdapter(adapter: A)
//        fun detachAdapter()
        fun onAdapterCreated(): Unit?
    }
}