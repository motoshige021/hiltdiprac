package com.github.motoshige021.hiltdiprac.data

import com.github.motoshige021.hiltdiprac.data.Result

interface TvProgramDataSource {
    fun seTvProgramDao(_tvProgramDao: TvProgramDao)

    suspend fun getAllPrograms(): Result<List<TvProgram>>

    suspend fun getProgram(id: String): Result<TvProgram>

    suspend fun loadData(completed: Boolean): Result<List<TvProgram>>

    suspend fun insert(program: TvProgram)

    suspend fun update(program: TvProgram)

    suspend fun delete(id: String) : Result<Boolean>
}
