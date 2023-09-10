package com.github.motoshige021.hiltdiprac.di

import android.content.Context
import com.github.motoshige021.hiltdiprac.LocaldbTaskRepository
import com.github.motoshige021.hiltdiprac.StubTaskRepository
import com.github.motoshige021.hiltdiprac.TaskRepository
import com.github.motoshige021.hiltdiprac.data.LocaldbDataSource
import com.github.motoshige021.hiltdiprac.data.TvProgramDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
/* Provideに変更
abstract class AppModule {
    @Singleton
    @Binds
    abstract fun bindTaskRepository(stubTaskRepository: StubTaskRepository) : TaskRepository
    */

    /* 開発要スタブ
    @Singleton
    @Provides
    fun provideTaskRepositor(@ApplicationContext context: Context) : TaskRepository {
        return StubTaskRepository(context)
    }
    */

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalProgramDataSource

    @Singleton
    @LocalProgramDataSource
    @Provides
    fun provideTvProgramDataSource(@ApplicationContext context: Context)
    :TvProgramDataSource {
        return LocaldbDataSource(context)
    }

    /* DAO-Sqlite3使用 */
    @Singleton
    @Provides
    //fun provideTaskRepositor(@ApplicationContext context: Context,
    fun provideTaskRepositor(
        @LocalProgramDataSource localdbDataSource: TvProgramDataSource): TaskRepository {
        return LocaldbTaskRepository(localdbDataSource)
    }

}
