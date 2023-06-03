package myapplication.WorkoutApp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM `cart-table` WHERE id = :id")
    suspend fun getCartItem(id: String): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCartItem(cartEntity: CartEntity)

    @Delete
    suspend fun deleteCartItem(cartEntity: CartEntity)

    @Query("SELECT * FROM `cart-table`")
    fun fetchAllCartItem(): Flow<List<CartEntity>>

}