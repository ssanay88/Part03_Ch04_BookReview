package com.example.part03_ch04_bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.part03_ch04_bookreview.model.Review

@Dao
interface ReviewDao {

    // 리뷰 액티비티에 들어갈 때 기존 리뷰를 불러오는 함수
    @Query("SELECT * FROM Review WHERE id == :id")
    fun getOneReview(id:Int): Review

    // 리뷰를 수정했을 때 수정 버튼 클릭 시 기존 리뷰에서 새로 쓴 리뷰로 수정
    // Insert만 사용할 경우 계속 추가만 되지만 onConflict를 사용하여 REPLACE로 설정하면 리뷰 내용이 매번 교체가 된다.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)


}