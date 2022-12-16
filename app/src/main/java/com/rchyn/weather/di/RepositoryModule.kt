package com.rchyn.weather.di

import com.rchyn.weather.data.repository.WeatherRepositoryImpl
import com.rchyn.weather.domain.repository.IWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindIWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): IWeatherRepository

}