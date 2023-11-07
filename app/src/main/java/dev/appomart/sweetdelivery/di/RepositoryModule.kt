package dev.appomart.sweetdelivery.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.appomart.sweetdelivery.data.repository.MapsRepository
import dev.appomart.sweetdelivery.data.repository.OrdersRepository
import dev.appomart.sweetdelivery.data.source.remote.MapsSource
import dev.appomart.sweetdelivery.data.source.remote.OrdersSource
import dev.appomart.sweetdelivery.domain.repository.IMapsRepository
import dev.appomart.sweetdelivery.domain.repository.IOrderRepository

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideOrdersRepository(
        ordersSource: OrdersSource
    ): IOrderRepository = OrdersRepository(ordersSource)

    @Provides
    fun provideMapsRepository(
        mapsSource: MapsSource
    ): IMapsRepository = MapsRepository(mapsSource)

}