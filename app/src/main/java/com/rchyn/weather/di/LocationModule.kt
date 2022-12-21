package com.rchyn.weather.di

import com.rchyn.weather.data.location.DefaultLocationTracker
import com.rchyn.weather.domain.location.ILocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindILocationTracker(
        defaultLocationTracker: DefaultLocationTracker
    ): ILocationTracker

}