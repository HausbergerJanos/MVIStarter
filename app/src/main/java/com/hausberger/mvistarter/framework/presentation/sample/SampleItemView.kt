package com.hausberger.mvistarter.framework.presentation.sample

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hausberger.mvistarter.business.domain.model.Sample
import kotlinx.android.synthetic.main.view_sample_item.view.*

class SampleItemView
constructor(
    itemView: View,
    private val interaction: SampleAdapter.Interaction?
) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Sample) = with(itemView) {
        title.text = item.title

        itemView.setOnClickListener {
            interaction?.onItemSelected(adapterPosition, item)
        }
    }
}