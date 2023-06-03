package myapplication.WorkoutApp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WishListDao {
    @Query("SELECT * FROM `wishlist-table` WHERE id = :id")
    suspend fun getWishListItem(id: String): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWishListItem(wishListEntity: WishListEntity)

    @Delete
    suspend fun deleteWishListItem(wishlistEntity: WishListEntity)

    @Query("SELECT * FROM `wishlist-table`")
    fun fetchAllWishListItem(): Flow<List<WishListEntity>>
}