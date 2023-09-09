package com.github.motoshige021.hiltdiprac.data

import com.github.motoshige021.hiltdiprac.data.Result.Success
import com.github.motoshige021.hiltdiprac.data.Result.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocaldbDataSource constructor()
    : TvProgramDataSource {

    private var tvProgramDao: TvProgramDao? = null

    override fun seTvProgramDao(_tvProgramDao: TvProgramDao) {
        tvProgramDao = _tvProgramDao
    }

    override suspend fun getAllPrograms(): Result<List<TvProgram>> = withContext(Dispatchers.IO) {
        return@withContext try {
            var _tvProgramDao = tvProgramDao ?: throw Exception("tvProgramDao is null")
            Success(_tvProgramDao.getAllPrograms())
        } catch(e: Exception) {
            Error(e)
        }
    }

    override suspend fun getProgram(id: String): Result<TvProgram> = withContext(Dispatchers.IO) {
        var _tvProgramDao = tvProgramDao ?: return@withContext Error(Exception("tvProgramDao is null"))
        val program = _tvProgramDao.getProgram(id)
        if (program != null) {
            return@withContext Success(program)
        } else {
            return@withContext Error(Exception("Program is not found"))
        }
    }

    override suspend fun loadData(completed: Boolean): Result<List<TvProgram>> = withContext(Dispatchers.IO) {
        return@withContext try {
            var _tvProgramDao = tvProgramDao ?: throw Exception("tvProgramDao is null")
            Success(_tvProgramDao.loadData(completed))
        } catch(e: Exception) {
            Error(e)
        }
    }

    override suspend fun insert(program: TvProgram) = withContext(Dispatchers.IO) {
        tvProgramDao!!.insert(program)
    }

    override suspend fun update(program: TvProgram) = withContext(Dispatchers.IO) {
        tvProgramDao!!.update(program)
    }

    override suspend fun delete(id: String) : Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            var _tvProgramDao = tvProgramDao ?: throw Exception("tvProgramDao is null")
            _tvProgramDao.delete(id)
            Success(true)
        } catch(e: Exception) {
            Error(e)
        }
    }
}