package com.onpu.app.base.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class ViewHolder<T: ViewBinding>(bindingView: T) : RecyclerView.ViewHolder(bindingView.root) {
    val binding = bindingView
}