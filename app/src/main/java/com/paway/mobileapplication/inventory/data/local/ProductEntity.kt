package com.paway.mobileapplication.inventory.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("productName")
    val name: String,
    @ColumnInfo("userId")
    val userId: String,
    @ColumnInfo("stock")
    val stock: Int,
    @ColumnInfo("initialStock")
    val initialStock: Int
)