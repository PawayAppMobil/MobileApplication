package com.paway.mobileapplication.inventory.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ProductDao {

    @Insert
    suspend fun insert(productEntity: ProductEntity)

    @Delete
    suspend fun delete(productEntity: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun fetchProductById(id: String): ProductEntity?

    @Query("UPDATE products SET stock = :stock WHERE id = :id")
    suspend fun updateStock(id: String, stock: Int)
}