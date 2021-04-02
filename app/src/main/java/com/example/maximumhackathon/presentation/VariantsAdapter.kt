package com.example.maximumhackathon.presentation

import android.view.View
import android.view.ViewGroup
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Word
import com.example.maximumhackathon.inflate
import com.example.maximumhackathon.presentation.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_variant.view.*


class VariantsAdapter: BaseRecyclerAdapter<Word, VariantsAdapter.VariantViewHolder>() {

    private var checkedPosition = - 1

    private var onItemClickListener: ((Int) -> Unit) = { position ->
        checkedPosition = position
        notifyDataSetChanged()
    }

    fun getCheckedWord(): Word? {
        if(checkedPosition == -1) {
            return null
        }
        return items[checkedPosition]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder {
        return VariantViewHolder(parent.inflate<View>(R.layout.item_variant, false))
    }

    inner class VariantViewHolder(itemView: View): ViewHolder<Word>(itemView) {
        override fun bindHolder(model: Word) {
            if (model.translate == "***"){
                itemView.textViewVariant.text = "(Перевод не найден)"
            } else {
                itemView.textViewVariant.text = model.translate
            }

            if(adapterPosition == checkedPosition) {
                itemView.imageViewChecked.visibility = View.VISIBLE
            } else {
                itemView.imageViewChecked.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener {
                onItemClickListener.invoke(adapterPosition)
            }
        }
    }


}