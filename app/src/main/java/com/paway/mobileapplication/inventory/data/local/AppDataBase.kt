package com.paway.mobileapplication.inventory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [ProductEntity::class], version = 1)
abstract class AppDataBase:RoomDatabase() {
    abstract fun getProductDao(): ProductDao
}