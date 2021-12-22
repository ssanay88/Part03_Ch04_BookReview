package com.example.part03_ch04_bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.part03_ch04_bookreview.model.History

@Dao
interface HistoryDao {

    // history 테이블에서 모든 정보를 가져오겠다. History 리스트로 반환
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    // 히스토리에 추가
    @Insert
    fun insertHistory(history: History)

    // 키워드를 통해 해당 기록 삭제
    @Query("DELETE FROM history WHERE keyword == :keyword")
    fun delete(keyword: String)

}