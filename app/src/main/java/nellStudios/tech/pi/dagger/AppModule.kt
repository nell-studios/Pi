package nellStudios.tech.pi.dagger

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import nellStudios.tech.pi.ui.adapters.ExploreTopicsAdapter
import nellStudios.tech.pi.ui.adapters.TopicDetailsAdapter
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePhoneAuthProvider(): PhoneAuthProvider = PhoneAuthProvider.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideExploreAdapter(): ExploreTopicsAdapter = ExploreTopicsAdapter()

    @Singleton
    @Provides
    fun provideTopicVideosAdapter(): TopicDetailsAdapter = TopicDetailsAdapter()
}