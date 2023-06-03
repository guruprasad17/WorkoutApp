package myapplication.WorkoutApp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist-table")

data class WishListEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image")
    val image: Int,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name="quantity")
    var quantity: Int
)
