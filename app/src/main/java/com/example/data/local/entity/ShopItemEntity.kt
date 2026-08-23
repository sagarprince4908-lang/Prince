package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: String, // "AVATAR", "THEME", "PET", "POTION", "TITLE"
    val iconEmoji: String,
    val priceCoins: Int,
    val priceGems: Int = 0,
    val isPurchased: Boolean = false,
    val isEquipped: Boolean = false,
    val requiredLevel: Int = 1,
    val statBoost: String = ""
)
