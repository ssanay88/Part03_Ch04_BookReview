package com.example.part03_ch04_bookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part03_ch04_bookreview.adapter.BookAdapter
import com.example.part03_ch04_bookreview.api.BookService
import com.example.part03_ch04_bookreview.databinding.ActivityMainBinding
import com.example.part03_ch04_bookreview.model.BestSellerDto
import com.example.part03_ch04_bookreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var adpater:BookAdapter
    private lateinit var bookService: BookService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()

        // retrofit 구현체
        val retrofit = Retrofit.Builder()
            .baseUrl("인터파크 Open API 기본 URL 입력")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // interface인 BookService를 retrofit을 이용하여 구현
        bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks("인터파크에서 발급받은 API키")
            .enqueue(object: Callback<BestSellerDto> {

                // api요청 성공 시
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (response.isSuccessful.not()) {
                        // 응답이 실패했을 경우
                        Log.e(TAG,"NOT SUCCESS")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG,it.toString())

                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        // adpater의 리스트를 해당 리스트로 변경한다.
                        adpater.submitList(it.books)
                    }
                }

                // api요청 실패 시
                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })

        // EditText에 키가 입력될 때 실행
        binding.searchEditText.setOnKeyListener { view, i, keyEvent ->
            // 엔터키가 눌렸을 경우
            // 키 이벤트에는 키를 누르는 액션(down)과 눌렀다 땠을 때(up) 액션이 있어서 여기서는 눌렀을 때를 구분해준다.
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }

    }

    private fun search(keyword:String) {

        bookService.getBookByName("인터파크에서 발급받은 API키",keyword)
            .enqueue(object: Callback<SearchBookDto> {

                // api요청 성공 시
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {
                    if (response.isSuccessful.not()) {
                        // 응답이 실패했을 경우
                        Log.e(TAG,"NOT SUCCESS")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG,it.toString())

                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        // adpater의 리스트를 해당 리스트로 변경한다.
                        adpater.submitList(it.books)
                    }
                }

                // api요청 실패 시
                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })

    }

    // recyclerView 관련 선언들
    fun initBookRecyclerView() {
        adpater = BookAdapter()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adpater
    }

    companion object {
        private const val TAG = "MainActivity"
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