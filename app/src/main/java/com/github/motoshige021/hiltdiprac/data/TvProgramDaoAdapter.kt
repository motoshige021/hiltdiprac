package com.github.motoshige021.hiltdiprac.data

import com.github.motoshige021.hiltdiprac.data.TvProgram
import com.github.motoshige021.hiltdiprac.data.Result
import com.github.motoshige021.hiltdiprac.data.Result.Success
import com.github.motoshige021.hiltdiprac.data.Result.Error
import com.github.motoshige021.hiltdiprac.data.TvProgramDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TvProgramDaoAdapter constructor(private val tvProgramDao: TvProgramDao) {
    suspend fun getAllPrograms(): Result<List<TvProgram>> = withContext(Dispatchers.IO) {
        return@withContext try {
            Success(tvProgramDao.getAllPrograms())
        } catch(e: Exception) {
            Error(e)
        }
    }

    suspend fun getProgram(id: String): Result<TvProgram> = withContext(Dispatchers.IO) {
        val program = tvProgramDao.getProgram(id)
        if (program != null) {
            return@withContext Success(program)
        } else {
            return@withContext Error(Exception("Program is not found"))
        }
    }

    suspend fun loadData(completed: Boolean): Result<List<TvProgram>> = withContext(Dispatchers.IO) {
        return@withContext try {
            Success(tvProgramDao.loadData(completed))
        } catch(e: Exception) {
            Error(e)
        }
    }

    suspend fun insert(program: TvProgram) = withContext(Dispatchers.IO) {
        tvProgramDao.insert(program)
    }

    suspend fun update(program: TvProgram) = withContext(Dispatchers.IO) {
        tvProgramDao.update(program)
    }
}