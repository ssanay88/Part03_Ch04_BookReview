package com.example.part03_ch04_bookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.part03_ch04_bookreview.api.BookService
import com.example.part03_ch04_bookreview.model.BestSellerDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // retrofit 구현체
        val retrofit = Retrofit.Builder()
            .baseUrl("인터파크 Open API 기본 URL 입력")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // interface인 BookService를 retrofit을 이용하여 구현
        val bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks("인터파크에서 발급받은 API키")
            .enqueue(object: Callback<BestSellerDto> {
                // api요청 성공 시
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    TODO("Not yet implemented")
                }

                // api요청 실패 시
                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

    }
}


/*

RecyclerView 사용하기
View Binding 사용하기
Retrofit 사용하기 ( API 호출 )
Glide 사용하기 ( 이미지 로딩 )
Android Room 사용하기
Open API 사용하기기

 */