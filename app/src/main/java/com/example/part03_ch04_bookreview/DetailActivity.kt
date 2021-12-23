package com.example.part03_ch04_bookreview

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.part03_ch04_bookreview.databinding.ActivityDetailBinding
import com.example.part03_ch04_bookreview.model.Book
import com.example.part03_ch04_bookreview.model.Review

// 책 리뷰를 작성할 액티비티
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        // 책 리뷰 액티비티가 시작되면 DB에 들어있던 리뷰를 불러와서 표시
        Thread {
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?:0)
            runOnUiThread {
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }.start()

        // saveBtn을 누를 경우 수정한 리뷰를 저장
        binding.saveBtn.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?: 0,
                        binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }

    }

}