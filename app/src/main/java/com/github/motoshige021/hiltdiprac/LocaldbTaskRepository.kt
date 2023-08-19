package com.github.motoshige021.hiltdiprac

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.github.motoshige021.hiltdiprac.data.AppDataBase
import com.github.motoshige021.hiltdiprac.data.TvProgram
import com.github.motoshige021.hiltdiprac.data.TvProgramDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocaldbTaskRepository @Inject constructor(_context: Context) : TaskRepository {
    private var database : AppDataBase
    private var tvprogramDao : TvProgramDao
    private val context: Context

    private var _tvProgramList = MutableLiveData<List<TvProgram>>()
    private val tvProgramList : LiveData<List<TvProgram>> = _tvProgramList
    private var _tvProgram = MutableLiveData<TvProgram>()
    private val tvProgram : LiveData<TvProgram> = _tvProgram

    private var filterType = TaskRepository.PROGRAM_TYPE.ALL.id

    init {
        context = _context
        database = Room.databaseBuilder(context.applicationContext,
            AppDataBase::class.java, "TvProgram.db")
            .build()
        tvprogramDao = database.tvProgramDao()
    }

    override fun obeserveList(): LiveData<List<TvProgram>> {
        return tvProgramList
    }

    override fun observerProgram(): LiveData<TvProgram> {
        return tvProgram
    }

    override suspend fun setupData() {
        coroutineScope {
            launch {
                if (tvprogramDao.getAllPrograms().size == 0) {
                    for (i in 1..18) {
                        tvprogramDao.insert(
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

    override suspend fun setProgramCopleted(program: TvProgram, completed: Boolean) {
        coroutineScope {
            launch {
                program.isCompleted = completed
                tvprogramDao.update(program)
            }
            loadData(filterType)
        }
    }

    override suspend fun loadData(type: Int) {
        coroutineScope {
            launch {
                filterType = type
                when (type) {
                    TaskRepository.PROGRAM_TYPE.COMPLETED.id -> {
                        _tvProgramList.value = tvprogramDao.loadData(true)
                    }
                    TaskRepository.PROGRAM_TYPE.ACTIVE.id -> {
                        _tvProgramList.value = tvprogramDao.loadData(false)
                    }
                    else -> { // TaskRepository.PROGRAM_TYPE.ALL.idを含む
                        _tvProgramList.value = tvprogramDao.getAllPrograms()
                    }
                }
            }
        }
    }

    override suspend fun getProgram(id: String) {
        coroutineScope {
            launch {
                tvprogramDao.getProgram(id)?.let { tvprogram ->
                    _tvProgram.value = tvprogram
                }
            }
        }
    }

    override fun getBroadID(id: Int): String {
        Log.d(Global.TAG, "LocalDbRepository::getBroadID:" + id.toString())
        return "data_" + id.toString()
    }
}