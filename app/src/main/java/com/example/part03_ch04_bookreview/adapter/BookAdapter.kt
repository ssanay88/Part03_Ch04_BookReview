package com.example.part03_ch04_bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.part03_ch04_bookreview.databinding.ItemBookBinding
import com.example.part03_ch04_bookreview.model.Book

// 리사이클러뷰 어댑터 클래스 생성, 함수를 인자로 받아와서 사용할 수 있다. ListAdapter(Book 데이터 클래스와 BookAdapter의 뷰홀더를 가진) 상속
class BookAdapter(val itemClickedListener: (Book) -> Unit): ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    // 뷰 홀더를 생성할 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    // 뷰가 뷰 홀더에 그려질 때
    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])    // 가지고 있는 리스트의 현재 포지션에 해당하는 데이터들을 뷰홀더에 적용시킨다.
    }

    // 이너 클래스로 뷰홀더 구현
    inner class BookItemViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: Book) {
            binding.titleTextView.text = bookModel.title
            binding.descroptionTextView.text = bookModel.description

            // 아이템 자체를 클릭할 경우
            binding.root.setOnClickListener {
                itemClickedListener(bookModel)    // 인자로 받아온 함수는 MainActivity에서 구현
            }

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