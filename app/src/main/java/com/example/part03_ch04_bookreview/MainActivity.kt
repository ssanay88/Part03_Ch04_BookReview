package com.example.part03_ch04_bookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.part03_ch04_bookreview.adapter.BookAdapter
import com.example.part03_ch04_bookreview.adapter.HistoryAdapter
import com.example.part03_ch04_bookreview.api.BookService
import com.example.part03_ch04_bookreview.databinding.ActivityMainBinding
import com.example.part03_ch04_bookreview.model.BestSellerDto
import com.example.part03_ch04_bookreview.model.History
import com.example.part03_ch04_bookreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var adpater:BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

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

                    // 검색하여 API 요청 성공 시 키워트에 대한 검색 기록 저장 함수수
                   saveSearchKeyword(keyword)

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
    private fun initBookRecyclerView() {
        adpater = BookAdapter()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adpater
    }

    // 검색 기록 창 보여주는 함수
    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()
        }
        binding.historyRecyclerView.isVisible = true
    }

    // 검색 기록 창 숨겨주는 함수수
    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    // 책을 검색 시 해당 keyword를 검색 기록에 저장하는 함수
    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null,keyword))
        }.start()
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