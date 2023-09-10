package com.github.motoshige021.hiltdiprac.data

import android.content.Context
import com.github.motoshige021.hiltdiprac.data.Result.*

class FakeDataSource : TvProgramDataSource {
    private var programList = ArrayList<TvProgram>()

    fun clearProgramList() { programList.clear() }

    fun addTestData(program: TvProgram) { programList.add(program) }

    fun getListSize(): Int { return programList.size }

    fun checkExistProgram(id: String) : Boolean {
        var isResult = false
        run loop@ {
            programList.forEach { program ->
                if (program.id == id) {
                    isResult = true
                    return@loop
                }
            }
        }
        return isResult
    }

    override fun setTvProgramDao() { }

    override suspend fun delete(id: String): Result<Boolean> {
        var isResult = false
        run loop@ {
            programList.forEach { program ->
                if (program.id == id) {
                    programList.remove(program)
                    isResult = true
                    return@loop
                }
            }
        }
        if (isResult) { return Success(true) }
        else { return Error(Exception("Not Found")) }
    }

    override suspend fun getAllPrograms(): Result<List<TvProgram>> {
        var allList : MutableList<TvProgram> = mutableListOf()
        programList.forEach { program ->
            allList.add(program)
        }
        return Success(allList)
    }

    override suspend fun getProgram(id: String): Result<TvProgram> {
        var program : TvProgram ? = null
        run loop@ {
            programList.forEach { _program ->
                if (_program.id == id) {
                    program = _program
                    return@loop
                }
            }
        }
        program?.let {
            return Success(it)
        }
        return Error(Exception("Not Found"))
    }

    override suspend fun insert(program: TvProgram) {
        programList.add(program)
    }

    override suspend fun loadData(completed: Boolean): Result<List<TvProgram>> {
        var allList : MutableList<TvProgram> = mutableListOf()
        programList.forEach { program ->
            if (program.isCompleted && completed) {
                allList.add(program)
            } else if (!(program.isCompleted) && !completed) {
                allList.add(program)
            }
        }
        return Success(allList)

    }

    override suspend fun update(program: TvProgram) {
        run loop@ {
            programList.forEach { _program ->
                if (_program.id == program.id) {
                    _program.title = program.title
                    _program.description = program.description
                    _program.isCompleted = program.isCompleted
                     return@loop
                }
            }
        }
    }
}