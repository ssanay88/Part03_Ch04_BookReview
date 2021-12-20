package com.example.part03_ch04_bookreview.model

import com.google.gson.annotations.SerializedName

data class Book(
    // @SerializedName : 서버에서는 itemId라고 가지고 오지만 여기서는 id로 선언하여서 동기화시켜주는 역할
    @SerializedName("itemId") val id:Long,    // 책 ID
    @SerializedName("title") val title:String,    // 책 제목
    @SerializedName("description") val description:String,    // 책 설명
    @SerializedName("coverSmalUrl") val coverSmallUrl:String    // 책 표지 이미지

)