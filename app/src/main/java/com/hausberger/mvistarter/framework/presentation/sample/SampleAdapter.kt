package com.hausberger.mvistarter.framework.presentation.sample

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.domain.model.Sample

class SampleAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Sample>() {

        override fun areItemsTheSame(oldItem: Sample, newItem: Sample): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Sample, newItem: Sample): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return SampleItemView(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_sample_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SampleItemView -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Sample>) {
        val commitCallback = Runnable {
            interaction?.restoreListPosition()
        }
        differ.submitList(list, commitCallback)
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Sample)
        fun restoreListPosition()
    }
}
