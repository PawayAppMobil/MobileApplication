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

    @Query("select * from products where id =:id")
    suspend fun fetchProductById(id: String): ProductEntity?
}