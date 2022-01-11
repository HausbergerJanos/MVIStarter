package com.hausberger.mvistarter.framework.presentation.sample

import androidx.recyclerview.widget.RecyclerView
import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.databinding.ViewSampleItemBinding

class SampleItemView
constructor(
    private val binding: ViewSampleItemBinding,
    private val interaction: SampleAdapter.Interaction?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Sample) = with(binding) {
        title.text = item.title

        itemView.setOnClickListener {
            interaction?.onItemSelected(adapterPosition, item)
        }
    }
}