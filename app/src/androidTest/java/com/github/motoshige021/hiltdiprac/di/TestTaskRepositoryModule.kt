package com.github.motoshige021.hiltdiprac.di

import android.content.Context
import com.github.motoshige021.hiltdiprac.StubTaskRepository
import com.github.motoshige021.hiltdiprac.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components =[SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestTaskRepositoryModule {
    @Singleton
    @Provides
    fun provideTaskRepositor(@ApplicationContext context: Context): TaskRepository {
        return StubTaskRepository(context)
    }
}