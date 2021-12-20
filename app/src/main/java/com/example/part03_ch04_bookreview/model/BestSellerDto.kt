package com.example.part03_ch04_bookreview.model

import com.google.gson.annotations.SerializedName

// 전체 API 결과를 받아오는 DTO , 책에 관한 세부 내용들은 item, 즉 books 리스트로 불러와진다.
data class BestSellerDto(
    @SerializedName("title") val title:String,
    @SerializedName("item") val books:List<Book>
)