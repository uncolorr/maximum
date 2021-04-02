package com.example.maximumhackathon.presentation

import android.view.View
import android.view.ViewGroup
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Lesson
import com.example.maximumhackathon.domain.model.LessonStatus
import com.example.maximumhackathon.domain.model.Test
import com.example.maximumhackathon.inflate
import com.example.maximumhackathon.presentation.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_lesson.view.*

class LessonsAdapter: BaseRecyclerAdapter<Lesson, BaseRecyclerAdapter.ViewHolder<Lesson>>() {

    var onItemClickListener: ((Lesson) -> Unit)? = null
    var onBlockedItemClickListener: (() -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Lesson> {
        return when (viewType) {
            PENDING_VIEW_HOLDER -> PendingViewHolder(parent.inflate<View>(R.layout.item_lesson, false))
            COMPLETED_VIEW_HOLDER -> CompletedViewHolder(parent.inflate<View>(R.layout.item_lesson, false))
            BLOCKED_VIEW_HOLDER -> BlockedViewHolder(parent.inflate<View>(R.layout.item_lesson, false))
            else -> BlockedViewHolder(parent.inflate<View>(R.layout.item_lesson, false))
        }
    }

    override fun setItems(items: List<Lesson>) {
        if(items.isEmpty()) {
            return
        }
        val lastCompleted = items.indexOfLast {
            it.status == LessonStatus.COMPLETED
        }
        if(lastCompleted == -1) {
            if(items.size > 1) {
                for (i in 1 until items.size) {
                    items[i].status = LessonStatus.BLOCKED
                }
            }
        } else {
            if(lastCompleted != items.size - 1) {
                for (i in lastCompleted + 2 until items.size) {
                    items[i].status = LessonStatus.BLOCKED
                }
            }
        }
        super.setItems(items)
    }

    private fun updateNextLesson(completedLesson: Lesson) {
        items.indexOfFirst {
            it.id == completedLesson.id
        }.let { index ->
            if(index != -1 && index < items.size - 1) {
                items[index + 1].status = LessonStatus.PENDING
            }
         }
    }

    fun updateLessonStatus(lesson: Lesson) {
        items.find {
            it.dbReference == lesson.dbReference
        }?.let {
            val oldStatus = it.status
            it.status = LessonStatus.COMPLETED
            if(oldStatus != LessonStatus.COMPLETED) {
                updateNextLesson(it)
            }
        }
    }

    inner class CompletedViewHolder(itemView: View): ViewHolder<Lesson>(itemView) {
        override fun bindHolder(model: Lesson) {
            itemView.textViewLessonName.text = model.name
            itemView.textViewDescription.text = model.description
            itemView.imageViewLessonStatus.setImageResource(R.drawable.ic_check)
            itemView.setOnClickListener {
                onItemClickListener?.invoke(model)
            }
        }
    }

    inner class PendingViewHolder(itemView: View): ViewHolder<Lesson>(itemView) {
        override fun bindHolder(model: Lesson) {
            itemView.textViewLessonName.text = model.name
            itemView.textViewDescription.text = model.description
            itemView.setOnClickListener {
                onItemClickListener?.invoke(model)
            }

        }
    }

    inner class BlockedViewHolder(itemView: View): ViewHolder<Lesson>(itemView) {
        override fun bindHolder(model: Lesson) {
            itemView.textViewLessonName.text = model.name
            itemView.textViewDescription.text = model.description
            itemView.imageViewLessonStatus.setImageResource(R.drawable.ic_lock)
            itemView.setOnClickListener {
                onBlockedItemClickListener?.invoke()
            }
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