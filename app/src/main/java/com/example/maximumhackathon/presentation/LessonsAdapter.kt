package com.example.maximumhackathon.presentation

import android.view.View
import android.view.ViewGroup
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Lesson
import com.example.maximumhackathon.domain.model.LessonStatus
import com.example.maximumhackathon.inflate
import com.example.maximumhackathon.presentation.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_lesson.view.*

class LessonsAdapter: BaseRecyclerAdapter<Lesson, BaseRecyclerAdapter.ViewHolder<Lesson>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Lesson> {
        return when (viewType) {
            PENDING_VIEW_HOLDER -> PendingViewHolder(parent.inflate<View>(R.layout.item_lesson, false))
            COMPLETED_VIEW_HOLDER -> CompletedViewHolder(parent.inflate<View>(R.layout.item_lesson, false))
            BLOCKED_VIEW_HOLDER -> BlockedViewHolder(parent.inflate<View>(R.layout.item_lesson, false))
            else -> BlockedViewHolder(parent.inflate<View>(R.layout.item_lesson, false))
        }
    }

    inner class CompletedViewHolder(itemView: View): ViewHolder<Lesson>(itemView) {
        override fun bindHolder(model: Lesson) {
            itemView.textViewLessonName.text = model.name
            itemView.textViewDescription.text = model.description
        }
    }

    inner class PendingViewHolder(itemView: View): ViewHolder<Lesson>(itemView) {
        override fun bindHolder(model: Lesson) {
            itemView.textViewLessonName.text = model.name
            itemView.textViewDescription.text = model.description
        }
    }

    inner class BlockedViewHolder(itemView: View): ViewHolder<Lesson>(itemView) {
        override fun bindHolder(model: Lesson) {
            itemView.textViewLessonName.text = model.name
            itemView.textViewDescription.text = model.description
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position].status) {
            LessonStatus.PENDING -> PENDING_VIEW_HOLDER
            LessonStatus.COMPLETED -> COMPLETED_VIEW_HOLDER
            LessonStatus.BLOCKED -> BLOCKED_VIEW_HOLDER
        }
    }

    companion object {
        private const val PENDING_VIEW_HOLDER = 1
        private const val COMPLETED_VIEW_HOLDER = 2
        private const val BLOCKED_VIEW_HOLDER = 3
    }
}