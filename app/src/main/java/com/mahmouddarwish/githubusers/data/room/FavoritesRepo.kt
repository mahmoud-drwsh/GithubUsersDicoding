package com.mahmouddarwish.githubusers.data.room

import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.use_cases.ManageFavoritesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepo @Inject constructor(
    private val dao: FavoritesDao,
) : ManageFavoritesUseCase {
    override suspend fun addFavorite(favorite: GitHubUser) {
        dao.upsert(favorite)
    }

    override suspend fun removeFavorite(favorite: GitHubUser) {
        dao.delete(favorite)
    }

    override fun getAll(): List<GitHubUser> = dao.getAll()
}