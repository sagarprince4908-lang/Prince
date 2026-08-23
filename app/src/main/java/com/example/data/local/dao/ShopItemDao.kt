package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ShopItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopItemDao {
    @Query("SELECT * FROM shop_items")
    fun getAllShopItems(): Flow<List<ShopItemEntity>>

    @Query("SELECT * FROM shop_items")
    suspend fun getAllShopItemsDirect(): List<ShopItemEntity>

    @Query("SELECT * FROM shop_items WHERE category = :category")
    fun getShopItemsByCategory(category: String): Flow<List<ShopItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShopItems(items: List<ShopItemEntity>)

    @Update
    suspend fun updateShopItem(item: ShopItemEntity)

    @Query("UPDATE shop_items SET isPurchased = 1 WHERE id = :id")
    suspend fun markPurchased(id: String)

    @Query("UPDATE shop_items SET isEquipped = 0 WHERE category = :category")
    suspend fun unequipCategory(category: String)

    @Query("UPDATE shop_items SET isEquipped = 1 WHERE id = :id")
    suspend fun setEquipped(id: String)
}
