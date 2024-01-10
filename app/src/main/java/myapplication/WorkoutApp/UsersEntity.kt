package myapplication.WorkoutApp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users-table")

data class UsersEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name="bmi")
    val bmi: Int
)
