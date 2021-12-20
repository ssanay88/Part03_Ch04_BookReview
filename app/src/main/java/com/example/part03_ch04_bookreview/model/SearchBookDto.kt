package com.example.part03_ch04_bookreview.model

import com.google.gson.annotations.SerializedName

data class SearchBookDto(
    @SerializedName("title") val title:String,
    @SerializedName("item") val books:List<Book>
)