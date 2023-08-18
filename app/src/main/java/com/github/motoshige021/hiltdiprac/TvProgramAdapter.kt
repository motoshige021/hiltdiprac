package com.github.motoshige021.hiltdiprac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.motoshige021.hiltdiprac.data.TvProgram
import com.github.motoshige021.hiltdiprac.databinding.TvprogramItemBinding
import com.github.motoshige021.hiltdiprac.ui.main.MainViewModel
import com.github.motoshige021.hiltdiprac.TvProgramAdapter.ViewHolder

class TvProgramAdapter(private val viewModel: MainViewModel)
    : ListAdapter<TvProgram, ViewHolder>(TvProgramDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TvprogramItemBinding.inflate(layoutInflater, parent, false)
        //val binding = TvprogramItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    class ViewHolder constructor(private val binding: TvprogramItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: MainViewModel, tvprogram: TvProgram) {
            binding.tvprogram = tvprogram
            binding.viewmodel = viewModel
        }
    }
}

class TvProgramDiffCallback: DiffUtil.ItemCallback<TvProgram>() {
    override fun areContentsTheSame(oldItem: TvProgram, newItem: TvProgram): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: TvProgram, newItem: TvProgram): Boolean {
        return oldItem.id == newItem.id
    }
}