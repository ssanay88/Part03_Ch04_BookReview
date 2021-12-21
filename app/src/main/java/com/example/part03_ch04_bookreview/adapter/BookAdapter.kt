package com.example.part03_ch04_bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.part03_ch04_bookreview.databinding.ItemBookBinding
import com.example.part03_ch04_bookreview.model.Book


class BookAdapter: ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    // 뷰 홀더를 생성할 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    // 뷰가 뷰 홀더에 그려질 때
    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class BookItemViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: Book) {
            binding.titleTextView.text = bookModel.title
            binding.descroptionTextView.text = bookModel.description

            // 서버에서 URL을 가지고 와서 coverImageView에 넣어준다.
            Glide
                .with(binding.coverImageView.context)
                .load(bookModel.coverSmallUrl)
                .into(binding.coverImageView)
        }

    }

    companion object {
        // 같은 아이템일 경우 굳이 새롭게 bind할 필요가 없기 때문에 구분하는 변수
        val diffUtil = object  : DiffUtil.ItemCallback<Book>() {
            // 두 아이템이 같은 아이템인가?
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            // 두 아이템의 내용이 같은가?
            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }


}