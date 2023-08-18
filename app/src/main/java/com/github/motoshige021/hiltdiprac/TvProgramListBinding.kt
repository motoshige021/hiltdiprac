package com.github.motoshige021.hiltdiprac

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.motoshige021.hiltdiprac.data.TvProgram

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<TvProgram>?) {
    items?.let {
        (listView.adapter as TvProgramAdapter).submitList(items)
    }
}