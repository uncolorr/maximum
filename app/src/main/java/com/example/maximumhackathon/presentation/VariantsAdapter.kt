package com.example.maximumhackathon.presentation

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Word
import com.example.maximumhackathon.inflate
import com.example.maximumhackathon.presentation.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_variant.view.*


class VariantsAdapter: BaseRecyclerAdapter<Word, VariantsAdapter.VariantViewHolder>() {

    var checkedPosition = - 1
    var rightPosition = - 1
    var wrongPosition = - 1

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

    fun setRightItem(position: Int){
        rightPosition = position
    }

    fun setWrongItem(position: Int){
        wrongPosition = position
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

            if (adapterPosition == rightPosition){
                itemView.backLayout.setBackgroundColor(Color.GREEN)
            }

            if (adapterPosition == wrongPosition){
                itemView.backLayout.setBackgroundColor(Color.RED)
            }

            itemView.setOnClickListener {
                onItemClickListener.invoke(adapterPosition)
            }
        }
    }


}