package myapplication.WorkoutApp

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [HistoryEntity::class, CartEntity::class, WishListEntity::class, UsersEntity::class], version = 5)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun CartDao(): CartDao
    abstract fun WishListDao() : WishListDao
    abstract fun UsersDao() : UsersDao

    companion object {
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java as Class<HistoryDatabase>,
                    "history_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as HistoryDatabase
        }
    }
}