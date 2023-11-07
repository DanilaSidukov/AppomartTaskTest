package dev.appomart.sweetdelivery.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.appomart.sweetdelivery.data.source.remote.MapsSource
import dev.appomart.sweetdelivery.data.source.remote.OrdersSource
import dev.appomart.sweetdelivery.domain.source.remote.IMapsSource
import dev.appomart.sweetdelivery.domain.source.remote.IOrdersSource

@Module
@InstallIn(SingletonComponent::class)
class SourceModule {

    @Provides
    fun provideOrdersSource(
        firestore: FirebaseFirestore
    ): IOrdersSource = OrdersSource(firestore)

    @Provides
    fun provideMapsSource(
        firestore: FirebaseFirestore
    ): IMapsSource = MapsSource(firestore)

}