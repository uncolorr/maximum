package com.example.maximumhackathon.presentation

import android.view.View
import android.view.ViewGroup
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Test
import com.example.maximumhackathon.domain.model.TestStatus
import com.example.maximumhackathon.inflate
import com.example.maximumhackathon.presentation.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_test.view.*

class TestsAdapter: BaseRecyclerAdapter<Test, BaseRecyclerAdapter.ViewHolder<Test>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Test> {
        return when (viewType) {
            PENDING_VIEW_HOLDER -> PendingViewHolder(parent.inflate<View>(R.layout.item_test, false))
            COMPLETED_VIEW_HOLDER -> CompletedViewHolder(parent.inflate<View>(R.layout.item_test, false))
            BLOCKED_VIEW_HOLDER -> BlockedViewHolder(parent.inflate<View>(R.layout.item_test, false))
            else -> BlockedViewHolder(parent.inflate<View>(R.layout.item_test, false))
        }
    }

    inner class CompletedViewHolder(itemView: View): ViewHolder<Test>(itemView) {
        override fun bindHolder(model: Test) {
            itemView.textViewTestName.text = model.name
            itemView.textViewDescription.text = model.description
            itemView.imageViewTestStatus.setImageResource(R.drawable.ic_check)
            itemView.textViewStats.text = model.stats
        }
    }

    inner class PendingViewHolder(itemView: View): ViewHolder<Test>(itemView) {
        override fun bindHolder(model: Test) {
            itemView.textViewTestName.text = model.name
            itemView.textViewDescription.text = model.description
            itemView.textViewStats.text = model.stats
        }
    }

    inner class BlockedViewHolder(itemView: View): ViewHolder<Test>(itemView) {
        override fun bindHolder(model: Test) {
            itemView.textViewTestName.text = model.name
            itemView.textViewDescription.text = model.description
            itemView.imageViewTestStatus.setImageResource(R.drawable.ic_lock)
            itemView.textViewStats.text = model.stats
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position].status) {
            TestStatus.PENDING -> PENDING_VIEW_HOLDER
            TestStatus.COMPLETED -> COMPLETED_VIEW_HOLDER
            TestStatus.BLOCKED -> BLOCKED_VIEW_HOLDER
        }
    }

    companion object {
        private const val PENDING_VIEW_HOLDER = 1
        private const val COMPLETED_VIEW_HOLDER = 2
        private const val BLOCKED_VIEW_HOLDER = 3
    }
}