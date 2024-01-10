package myapplication.WorkoutApp
import androidx.room.*
import kotlinx.coroutines.flow.Flow

    @Dao
    interface UsersDao {
        @Query("SELECT COUNT(*) FROM `users-table`")
        fun getRecordCount(): Int?

        @Query("SELECT email FROM `users-table` WHERE email = :email")
        fun getEmailByEmail(email: String): String?

        @Query("SELECT password FROM `users-table` WHERE email = :email")
        fun getPasswordByEmail(email: String): String?

        @Insert
        fun insertUser(user: UsersEntity)

        @Query("SELECT id FROM `users-table` WHERE email = :email")
         fun getUserIdByEmail(email: String): Int?
    }
