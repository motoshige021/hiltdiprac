package com.github.motoshige021.hiltdiprac.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TvProgram::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun tvProgramDao(): TvProgramDao;
}