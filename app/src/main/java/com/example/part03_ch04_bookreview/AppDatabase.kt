package com.example.part03_ch04_bookreview

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.part03_ch04_bookreview.dao.HistoryDao
import com.example.part03_ch04_bookreview.dao.ReviewDao
import com.example.part03_ch04_bookreview.model.History
import com.example.part03_ch04_bookreview.model.Review

// 처음에 History class만 있을 때 DB를 생성했기 때문에 version 1의 DB가 생성됐기 때문에
// DB에 다른 class나 수정사항이 생길 경우 version을 다르게 만들어주거나 , 기존 DB를 삭제한 후 실행하여야 한다.
@Database(entities = [History::class, Review::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao

}


// 액티비티에서 DB를 선언할 수 있게 하는 함수
// DB version을 수정해야 할 경우 migration을 통해 업그레이드 해줘야 한다.
fun getAppDatabase(context: Context): AppDatabase {

    // version 1에서 2로 넘어가는 것을 알려주기 위한 코드
    val migration_1_2 = object : Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE 'REVIEW' ('id' INTEGER, 'review' TEXT," + "PRIMARY_KEY('id'))")
        }
    }

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "BookSearchDB"
    )
        .addMigrations(migration_1_2)    // DB가 version 1에서 2로 넘어갈 때 달라지는 점을 알려줘야 한다.
        .build()
}