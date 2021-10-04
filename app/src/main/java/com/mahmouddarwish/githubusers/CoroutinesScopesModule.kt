package com.mahmouddarwish.githubusers

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


/**
 * This code in its entirety is taken from a medium article by one of the Android development
 * team from google.
 * This is the link:
 * https://medium.com/androiddevelopers/create-an-application-coroutinescope-using-hilt-dd444e721528
 * */
@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @ApplicationScope
    @Singleton // Provide always the same instance
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class ApplicationScope
}
