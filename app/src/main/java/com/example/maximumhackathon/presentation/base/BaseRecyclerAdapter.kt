package com.example.maximumhackathon.presentation.base

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<M : Any, VH : BaseRecyclerAdapter.ViewHolder<M>> : RecyclerView.Adapter<VH>() {

    /** Collection for adapter's items  */
    protected val items = ArrayList<M>()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindHolder(items[position])
    }

    fun getItems(): List<M> {
        return items
    }

    open fun getDiffCallBack(oldItems: List<M>, newItems: List<M>): DiffUtil.Callback? {
        return null
    }

    /**
     * Sets adapter items
     * @param items items to show
     */
    open fun setItems(items: List<M>) {
        val diffCallback = getDiffCallBack(this.items, items)
        if (diffCallback == null) {
            setNewData(items)
            notifyDataSetChanged()
        } else {
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            setNewData(items)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    open fun getItem(position: Int): M {
        return items[position]
    }

    private fun setNewData(data: List<M>) {
        this.items.clear()
        this.items.addAll(data)
    }

    open fun clear() {
        val count = itemCount
        items.clear()
        notifyItemRangeRemoved(0, count)
    }

    abstract class ViewHolder<M>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bindHolder(model: M)
    }
}
