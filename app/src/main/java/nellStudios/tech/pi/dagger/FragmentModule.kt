package nellStudios.tech.pi.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import nellStudios.tech.pi.ui.adapters.ExploreTopicsAdapter
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    fun provideExploreAdapter(): ExploreTopicsAdapter = ExploreTopicsAdapter()
}