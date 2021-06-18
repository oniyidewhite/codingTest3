package com.oblessing.mobliepay.di

import com.oblessing.mobliepay.core.support.AssistedViewModelFactory
import com.oblessing.mobliepay.core.support.MavericksViewModelComponent
import com.oblessing.mobliepay.core.support.ViewModelKey
import com.oblessing.mobliepay.screens.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun provideNewsListingViewModelFactory(factory: SearchViewModel.Factory): AssistedViewModelFactory<*, *>
}