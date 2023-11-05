package com.test.appomarttaskertest.di

import com.google.firebase.firestore.FirebaseFirestore
import com.test.appomarttaskertest.data.repository.OrdersRepository
import com.test.appomarttaskertest.data.source.remote.OrdersSource
import com.test.appomarttaskertest.domain.repository.IOrderRepository
import com.test.appomarttaskertest.domain.source.IOrdersSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideOrdersSource(
        firestore: FirebaseFirestore
    ): IOrdersSource = OrdersSource(firestore)

    @Provides
    fun provideOrdersRepository(
        ordersSource: OrdersSource
    ): IOrderRepository = OrdersRepository(ordersSource)

}