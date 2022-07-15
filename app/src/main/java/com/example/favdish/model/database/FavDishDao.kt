package com.example.favdish.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.favdish.model.entities.FavDish

@Dao
interface FavDishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavDishDetails(favDish: FavDish)
}