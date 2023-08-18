package com.github.motoshige021.hiltdiprac.data

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Dao

@Dao
interface TvProgramDao {
    @Query("SELECT * FROM tvprograms")
    fun observeProgram(): LiveData<List<TvProgram>>;

    @Query("SELECT * FROM tvprograms")
    /*suspend*/ fun getAllPrograms(): List<TvProgram>;

    @Query("SELECT * FROM tvprograms WHERE eventid = :id")
    /*suspend*/ fun getProgram(id: String): TvProgram?

    @Query("SELECT * FROM tvprograms WHERE completed = :completed")
    /*suspend*/ fun loadData(completed: Boolean): List<TvProgram>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    /*suspend*/ fun insert(program: TvProgram);

    @Update
    /*suspend*/ fun update(program: TvProgram);

}