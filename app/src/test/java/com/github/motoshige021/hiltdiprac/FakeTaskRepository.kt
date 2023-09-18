package com.github.motoshige021.hiltdiprac

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.motoshige021.hiltdiprac.data.TvProgram

class FakeTaskRepository : TaskRepository {

    private var _tvProgramList = MutableLiveData<List<TvProgram>>()
    private val tvProgramList : LiveData<List<TvProgram>> = _tvProgramList
    private var _tvProgram = MutableLiveData<TvProgram>()
    private val tvProgram : LiveData<TvProgram> = _tvProgram
    private var _deleteResult = MutableLiveData<Boolean>()
    private val deleteResult: LiveData<Boolean> = _deleteResult

    private var testList : ArrayList<TvProgram>

    private var filterType = TaskRepository.PROGRAM_TYPE.ALL.id

    init {
        testList = ArrayList<TvProgram>()
    }

    override fun clearProgramList() {
        testList.clear()
    }

    override fun addProgramToList(program: TvProgram) {
        testList.add(program)
    }

    fun setFilterType(type: Int) {
        filterType = type
    }

    override fun obeserveList(): LiveData<List<TvProgram>> {
        return tvProgramList
    }

    override fun observerProgram(): LiveData<TvProgram> {
        return tvProgram
    }

    override fun getBroadID(id: Int): String {
        return "FakeID-" + id.toString()
    }

    override suspend fun getProgram(id: String) {
        run loop@{
            testList.forEach { program ->
                if (program.id == id) {
                    _tvProgram.value = program
                    return@loop
                }
            }
        }
    }

    override suspend fun loadData(type: Int) {
        filterType = type
        var arrayList = ArrayList<TvProgram>()
        if (type == TaskRepository.PROGRAM_TYPE.ALL.id) {
            arrayList = testList
        }  else if (type == TaskRepository.PROGRAM_TYPE.ACTIVE.id) {
            testList.forEach { tvprogram ->
                if (!tvprogram.isCompleted) {
                    arrayList.add(tvprogram)
                }
            }

        } else if (type == TaskRepository.PROGRAM_TYPE.COMPLETED.id) {
            testList.forEach { tvprogram ->
                if (tvprogram.isCompleted) {
                    arrayList.add(tvprogram)
                }
            }
        }
        _tvProgramList.value = arrayList
    }

    override suspend fun setProgramCopleted(in_program: TvProgram, completed: Boolean) {
        testList.forEach { tvProgram ->
            if (in_program.id == tvProgram.id) {
                tvProgram.isCompleted = completed
            }
        }
        loadData(filterType)
    }

    override suspend fun setupData() {
    }

    override fun oberverDeleteResult(): LiveData<Boolean> {
        return  deleteResult
    }

    override suspend fun deleteProgram(id: String) {
        var isDelete = false
        run loop@ {
            testList.forEach { program ->
                if (id == program.id) {
                    testList.remove(program)
                    isDelete = true
                    return@loop
                }
            }
        }
        _deleteResult.value = isDelete
    }

    fun checkExistProgram(id: String): Boolean {
        var ret = false
        run loop@ {
            testList.forEach { program ->
                if (program.id == id) {
                    ret = true
                    return@loop
                }
            }
        }
        return ret
    }
}