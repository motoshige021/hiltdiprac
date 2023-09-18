package com.github.motoshige021.hiltdiprac

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.github.motoshige021.hiltdiprac.data.*
import com.github.motoshige021.hiltdiprac.util.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocaldbTaskRepository @Inject constructor(_localdbDataSource: TvProgramDataSource)
    : TaskRepository {
    private var localdbDataSource: TvProgramDataSource

    private var _tvProgramList = MutableLiveData<List<TvProgram>>()
    private val tvProgramList : LiveData<List<TvProgram>> = _tvProgramList
    private var _tvProgram = MutableLiveData<TvProgram>()
    private val tvProgram : LiveData<TvProgram> = _tvProgram
    private var _deleteResult = MutableLiveData<Boolean>()
    private val deleteResult : LiveData<Boolean> = _deleteResult

    private var filterType = TaskRepository.PROGRAM_TYPE.ALL.id

    init {
        localdbDataSource = _localdbDataSource
        localdbDataSource.setTvProgramDao()
    }

    override fun obeserveList(): LiveData<List<TvProgram>> {
        return tvProgramList
    }

    override fun observerProgram(): LiveData<TvProgram> {
        return tvProgram
    }

    override fun oberverDeleteResult() : LiveData<Boolean> {
        return deleteResult
    }

    override suspend fun setupData() {
        wrapEspressoIdlingResource {
            coroutineScope {
                launch {
                    var result = localdbDataSource.getAllPrograms()
                    if (result is Result.Success && result.data.size == 0) {
                        for (i in 1..18) {
                            localdbDataSource.insert(
                                TvProgram(
                                    "Program" + (i * 10).toString() + "_db",
                                    "Description" + (i * 100).toString() + "_db",
                                    (i % 2 == 0)
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override suspend fun setProgramCopleted(program: TvProgram, completed: Boolean) {
        coroutineScope {
            launch {
                program.isCompleted = completed
                localdbDataSource.update(program)
            }
            loadData(filterType)
        }
    }

    override suspend fun loadData(type: Int) {
        wrapEspressoIdlingResource {
            coroutineScope {
                launch {
                        filterType = type
                        when (type) {
                            TaskRepository.PROGRAM_TYPE.COMPLETED.id -> {
                                val result = localdbDataSource.loadData(true)
                                if (result is Result.Success) {
                                    result.data?.let { list ->
                                        _tvProgramList.value = list
                                    }
                                } else if (result is Result.Error) {
                                    result.exception.message?.let { Log.d(Global.TAG, it) }
                                }
                            }
                            TaskRepository.PROGRAM_TYPE.ACTIVE.id -> {
                                val result = localdbDataSource.loadData(false)
                                if (result is Result.Success) {
                                    result.data?.let { list ->
                                        _tvProgramList.value = list
                                    }
                                } else if (result is Result.Error) {
                                    result.exception.message?.let { Log.d(Global.TAG, it) }
                                }
                            }
                            else -> { // TaskRepository.PROGRAM_TYPE.ALL.idを含む
                                val result = localdbDataSource.getAllPrograms()
                                if (result is Result.Success) {
                                    result.data?.let { list ->
                                        _tvProgramList.value = list
                                    }
                                } else if (result is Result.Error) {
                                    result.exception.message?.let { Log.d(Global.TAG, it) }
                                }
                            }
                        }
                }
            }
        }
    }

    override suspend fun getProgram(id: String) {
        coroutineScope {
            launch {
                var result = localdbDataSource.getProgram(id)
                if (result is Result.Success) {
                    result.data?.let { program ->
                        _tvProgram.value = program
                    }
                } else  if (result is Result.Error) {
                    result.exception.message?.let { Log.d(Global.TAG, it) }
                }
            }
        }
    }

    override fun getBroadID(id: Int): String {
        Log.d(Global.TAG, "LocalDbRepository::getBroadID:" + id.toString())
        return "data_" + id.toString()
    }

    override suspend fun deleteProgram(id: String) {
        Log.d(Global.TAG,"LocalDbRepository::deleteProgram:" +id.toString())
        coroutineScope {
            launch {
                var result = localdbDataSource.delete(id)
                if (result is Result.Success) {
                    _deleteResult.value = true
                } else if (result is Result.Error) {
                    _deleteResult.value = false
                }
            }
        }
    }

    override fun addProgramToList(program: TvProgram) {
        TODO("Not yet implemented")
    }

    override fun clearProgramList() {
        TODO("Not yet implemented")
    }
}