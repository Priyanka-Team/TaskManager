package com.bornfire.taskmanagement

import android.content.Context
import androidx.room.Room
import com.bornfire.taskmanagement.data.local.database.AppDatabase
import com.bornfire.taskmanagement.data.local.dao.CategoryDao
import com.bornfire.taskmanagement.data.local.dao.TaskDao
import com.bornfire.taskmanagement.data.repository.CategoryRepository
import com.bornfire.taskmanagement.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideCategoryRepository(dao: CategoryDao): CategoryRepository {
        return CategoryRepository(dao)
    }

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideTaskRepository(dao: TaskDao): TaskRepository {
        return TaskRepository(dao)
    }
}
