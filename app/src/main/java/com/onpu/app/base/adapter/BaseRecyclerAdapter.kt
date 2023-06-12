package com.onpu.app.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerAdapter<VB: ViewBinding> : RecyclerView.Adapter<ViewHolder<VB>>() {

    protected abstract fun ViewGroup.layout(): VB

    abstract fun VB.onCreateView(position: Int)

    protected fun ViewGroup.layoutInflater(): LayoutInflater = LayoutInflater.from(context)

    protected val ViewBinding.context: Context get() = root.context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        return ViewHolder(parent.layout())
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) = holder
        .binding
        .onCreateView(position)
}