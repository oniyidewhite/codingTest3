package com.oblessing.mobliepay.di


import com.oblessing.mobliepay.core.EntityMapper
import com.oblessing.mobliepay.model.PageResult
import com.oblessing.mobliepay.network.PlaceWebService
import com.oblessing.mobliepay.network.PlaceListEntity
import com.oblessing.mobliepay.repository.PlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        resultMapper: EntityMapper<PlaceListEntity?, PageResult?>,
        webService: PlaceWebService,
        @Named("limit") limit: Int
    ) = PlaceRepository(webService, resultMapper, limit)

    @Provides
    @Named("limit")
    @Singleton
    fun provideQueryResultLimit() = 20
}