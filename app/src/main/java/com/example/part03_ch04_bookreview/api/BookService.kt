package com.example.part03_ch04_bookreview.api

import com.example.part03_ch04_bookreview.model.BestSellerDto
import com.example.part03_ch04_bookreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    // 베스트 셀러 데이터들을 불러오는 요청 , 요청시 BestSellerDto형태로 묶여서 가져온다.
    @GET("")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ):Call<BestSellerDto>    // BestSellerDto를 반환

    // get : 데이터를 받아오는 요청
    // 책 제목으로 정보들을 불러오는 요청
    @GET("")
    fun getBookByName(
        @Query("key") apiKey:String,
        @Query("query") keyword:String
    ):Call<SearchBookDto>    // SearchBookDto를 반환환


}