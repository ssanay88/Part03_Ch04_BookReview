package com.example.part03_ch04_bookreview

import android.content.Intent
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

    private lateinit var binding:ActivityMainBinding    // 뷰 바인딩을 위한 변수
    private lateinit var adpater:BookAdapter    // 책을 보여줄 리사이클려뷰에 적용할 어댑터
    private lateinit var historyAdapter: HistoryAdapter    // 검색 기록을 보여줄 리사이클러뷰에 적용할 어댑터
    private lateinit var bookService: BookService    // Rest Api 통신으로 책에 대한 정보를 받아올 서비스

    private lateinit var db: AppDatabase    // 앱의 내부에 저장할 DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()    // 책에 대한 정보를 받아와서 리사이클러뷰에 보여주도록 한다.
        initHistoryRecyclerView()    // 검색 기록을 보여줄 리사이클러뷰에 대해 설정
        initSearchEditText()    // 검색창에 대한 설정, 클릭 시 행동에 대해 구현

        db = getAppDatabase(this)    // 앱의 DB를 불러와준다.

        // retrofit 구현체
        val retrofit = Retrofit.Builder()
            .baseUrl("인터파크 Open API 기본 URL 입력")    // 기본 URL 설정
            .addConverterFactory(GsonConverterFactory.create())    // 역직렬화를 위한 컨버터 설정
            .build()


        // interface인 BookService를 retrofit을 이용하여 구현
        bookService = retrofit.create(BookService::class.java)    // API통신을 통해 JSON파일들을 받아오는 함수를 가지는 인터페이스

        // 생성한 인터페이스에서 함수를 통해 원하는 데이터를 불러와서 사용
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

                        // BestSellerDto가 가지고 있는 book데이터 리스트들에 하나씩 접근
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



    }

    // 검색 기능 구현
    private fun search(keyword:String) {

        bookService.getBookByName("인터파크에서 발급받은 API키",keyword)
            .enqueue(object: Callback<SearchBookDto> {

                // api요청 성공 시
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {

                    // 검색 성공 시 검색 기록 숨긴다.
                    hideHistoryView()
                    // 검색하여 API 요청 성공 시 키워트에 대한 검색 기록 저장 함수
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
                        adpater.submitList(it.books)    // 책을 보여주는 리사이클러뷰에 추가
                    }
                }

                // api요청 실패 시
                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {

                    hideHistoryView()
                    Log.e(TAG, t.toString())

                }

            })

    }

    // recyclerView 관련 선언들
    private fun initBookRecyclerView() {
        // 어댑터 객체 생성 , 아이템을 클릭 했을 때 행동도 선언
        adpater = BookAdapter(itemClickedListener = {
            val intent = Intent(this,DetailActivity::class.java)
            intent.putExtra("bookModel",it)    // Book 모델 전체를 보내기 위해선 직렬화를 해야한다.
            startActivity(intent)
        })

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)    // 리사이클러뷰 레이아웃 매니저 연결
        binding.bookRecyclerView.adapter = adpater    // 어댑터 또한 연결
    }

    // 검색 기록 recyclerView 관련 선언들
    private fun initHistoryRecyclerView() {
        // 검색 기록 리사이클러뷰에 사용할 어댑터 선언,
        historyAdapter = HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)    // 람다식 인자를 설정
        })

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)    // 리사이클러뷰 레이아웃 매니저 연결
        binding.historyRecyclerView.adapter = historyAdapter


    }

    // EditText에 키가 입력될 때 실행
    private fun initSearchEditText() {

        binding.searchEditText.setOnKeyListener { view, i, keyEvent ->
            // 엔터키가 눌렸을 경우
            // 키 이벤트에는 키를 누르는 액션(down)과 눌렀다 땠을 때(up) 액션이 있어서 여기서는 눌렀을 때를 구분해준다.
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())    // 입력한 내용으로 검색
                return@setOnKeyListener true    // true 반환
            }

            return@setOnKeyListener false    // false 반환
        }

        binding.searchEditText.setOnTouchListener {v,event ->
            // EditText가 클릭된 경우 해당 액션이 EditText를 누르는 경우 실행
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    // 검색 기록 창 보여주는 함수
    private fun showHistoryView() {
        // 다른 스레드에서 진행 -> 백그라운드에서 바로 바로 실행
        Thread {
            val keywords = db.historyDao().getAll().reversed()    // DB에 저장된 기록들을 불러온다. 최신순으로

            // UI 스레드에 적용
            runOnUiThread {
                binding.historyRecyclerView.isVisible = true    // 검색 기록 리사이클러뷰 가시성을 ON
                // orEmpty : keywords가 null일 수도 있기 때문에 이 경우 empty로 반환
                historyAdapter.submitList(keywords.orEmpty())    // 어댑터에 추가
            }
        }.start()

        binding.historyRecyclerView.isVisible = true    // 검색 기록 창을 계속 보여준다.
    }

    // 검색 기록 창 숨겨주는 함수수
    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false    // 취소 버튼 클릭 시 검색 기록창 OFF
    }

    // 책을 검색 시 해당 keyword를 검색 기록에 저장하는 함수
    private fun saveSearchKeyword(keyword: String) {
        Thread {
            // DB에 키워드를 이용하여 데이터 추가
            db.historyDao().insertHistory(History(null,keyword))
        }.start()
    }

    // 검색 기록 창에서 삭제 버튼 클릭 시 이벤트
    private fun deleteSearchKeyword(keyword: String) {
        // 백그라운드에서 진행
        Thread {
            // DB에서 키워드에 따른 데이터 삭제
            db.historyDao().delete(keyword)
            // TODO 뷰 갱신
            showHistoryView()    // 리사이클러뷰를 업데이트
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