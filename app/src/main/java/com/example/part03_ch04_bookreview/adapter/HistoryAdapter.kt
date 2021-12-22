package com.example.part03_ch04_bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.part03_ch04_bookreview.databinding.ItemBookBinding
import com.example.part03_ch04_bookreview.databinding.ItemHistoryBinding
import com.example.part03_ch04_bookreview.model.Book
import com.example.part03_ch04_bookreview.model.History

class HistoryAdapter(val historyDeleteClickedListener: (String) -> Unit): ListAdapter<History , HistoryAdapter.HistoryItemViewHolder>(diffUtil) {

    // 뷰 홀더를 생성할 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    // 뷰가 뷰 홀더에 그려질 때
    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(historyModel: History) {
            binding.historyTextView.text = historyModel.keyword

        }

    }

    companion object {
        // 같은 아이템일 경우 굳이 새롭게 bind할 필요가 없기 때문에 구분하는 변수
        val diffUtil = object  : DiffUtil.ItemCallback<History>() {
            // 두 아이템이 같은 아이템인가?
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            // 두 아이템의 내용이 같은가?
            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }

}