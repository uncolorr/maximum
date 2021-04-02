package com.example.maximumhackathon.presentation

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Word
import com.example.maximumhackathon.inflate
import com.example.maximumhackathon.presentation.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_variant.view.*


class VariantsAdapter : BaseRecyclerAdapter<Word, VariantsAdapter.VariantViewHolder>() {

    private var checkedPosition = -1

    private var isLightAnswer: Boolean = false

    private var rightWord: Word? = null

    private var onItemClickListener: ((Int) -> Unit) = { position ->
        if(!isLightAnswer) {
            checkedPosition = position
            notifyDataSetChanged()
        }
    }

    fun getCheckedWord(): Word? {
        if (checkedPosition == -1) {
            return null
        }
        return items[checkedPosition]
    }

    fun resetCheck() {
        checkedPosition = -1
        isLightAnswer = false
        notifyDataSetChanged()
    }

    fun showAnswer(rightWord: Word) {
        if (checkedPosition == -1) {
            return
        }
        isLightAnswer = true
        this.rightWord = rightWord
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder {
        return VariantViewHolder(parent.inflate<View>(R.layout.item_variant, false))
    }

    inner class VariantViewHolder(itemView: View) : ViewHolder<Word>(itemView) {
        @SuppressLint("NewApi")
        override fun bindHolder(model: Word) {
            if (model.translate == "***") {
                itemView.textViewVariant.text = "(Перевод не найден)"
            } else {
                itemView.textViewVariant.text = model.translate
            }

            if (adapterPosition == checkedPosition) {
                itemView.imageViewChecked.visibility = View.VISIBLE
            } else {
                itemView.imageViewChecked.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener {
                onItemClickListener.invoke(adapterPosition)
            }

            if (isLightAnswer) {
                if (adapterPosition == checkedPosition && rightWord?.translate != model.translate) {
                    itemView.layout.setCardBackgroundColor(
                        itemView.resources.getColor(
                            android.R.color.holo_red_light,
                            null
                        )
                    )
                }

                else if (adapterPosition == checkedPosition && rightWord?.translate == model.translate) {
                    itemView.layout.setCardBackgroundColor(
                        itemView.resources.getColor(
                            android.R.color.holo_green_light,
                            null
                        )
                    )
                }
                else if (adapterPosition != checkedPosition && rightWord?.translate == model.translate) {
                    itemView.layout.setCardBackgroundColor(
                        itemView.resources.getColor(
                            android.R.color.holo_green_light,
                            null
                        )
                    )
                } else {
                    itemView.layout.setCardBackgroundColor(
                        itemView.resources.getColor(
                            android.R.color.white,
                            null
                        )
                    )
                }
            } else {
                itemView.layout.setCardBackgroundColor(
                    itemView.resources.getColor(
                        android.R.color.white,
                        null
                    )
                )
            }
        }
    }


}