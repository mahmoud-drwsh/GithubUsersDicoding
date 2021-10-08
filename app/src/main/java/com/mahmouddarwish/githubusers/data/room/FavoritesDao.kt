package com.mahmouddarwish.githubusers.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert(onConflict = REPLACE)
    fun upsert(favorite: GitHubUser)

    @Delete
    fun delete(favorite: GitHubUser)

    @Query("SELECT * FROM GitHubUser ORDER BY login")
    fun getAll(): List<GitHubUser>

    @Query("SELECT * FROM GitHubUser ORDER BY login")
    fun getAllFlow(): Flow<List<GitHubUser>>
}
