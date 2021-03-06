package com.mahmouddarwish.githubusers.data.repos

import com.mahmouddarwish.githubusers.CoroutinesScopesModule
import com.mahmouddarwish.githubusers.data.room.FavoritesDao
import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.use_cases.ManageFavoritesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepo @Inject constructor(
    private val dao: FavoritesDao,
    @CoroutinesScopesModule.ApplicationScope val coroutineScope: CoroutineScope,
) : ManageFavoritesUseCase {
    override fun addFavorite(favorite: GitHubUser) {
        coroutineScope.launch(IO) { dao.upsert(favorite) }
    }

    override fun removeFavorite(favorite: GitHubUser) {
        coroutineScope.launch(IO) { dao.delete(favorite) }
    }

    override fun getAllFlow(): Flow<List<GitHubUser>> = dao.getAllFlow()
}