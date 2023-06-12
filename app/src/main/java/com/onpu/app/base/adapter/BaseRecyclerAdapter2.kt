//package com.onpu.app.base.adapter
//
//
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.onpu.domain.mvp.base.adapter.BaseAdapterMvp.*
//
//abstract class BaseRecyclerAdapter2<P : BasePresenter<A>, A : BaseAdapter>(
//    protected val presenter: P
//) : RecyclerView.Adapter<ViewHolder2>(), BaseAdapter {
//
//    protected abstract val layoutRes: Int
//
//    abstract fun ViewHolder2.onCreateView(position: Int)
//
//    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//        super.onAttachedToRecyclerView(recyclerView)
//        presenter.attachAdapter(this as A)
//        presenter.onAdapterCreated()
//    }
//
//    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
//        presenter.detachAdapter()
//        super.onDetachedFromRecyclerView(recyclerView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
//        return ViewHolder2(parent, layoutRes)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder2, position: Int) = holder
//        .onCreateView(position)
//}