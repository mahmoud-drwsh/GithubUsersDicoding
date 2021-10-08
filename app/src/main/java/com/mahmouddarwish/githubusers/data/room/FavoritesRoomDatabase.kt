package com.mahmouddarwish.githubusers.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mahmouddarwish.githubusers.domain.models.GitHubUser

@Database(entities = [GitHubUser::class], version = 1, exportSchema = false)
abstract class FavoritesRoomDatabase : RoomDatabase() {
    abstract fun FavoritesDao(): FavoritesDao
}