package com.github.motoshige021.hiltdiprac

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.motoshige021.hiltdiprac.data.TvProgram
import javax.inject.Inject

class StubTaskRepository @Inject constructor(context: Context) : TaskRepository {
    private var _tvProgramList = MutableLiveData<List<TvProgram>>()
    private val tvProgramList : LiveData<List<TvProgram>> = _tvProgramList
    private var _tvProgram = MutableLiveData<TvProgram>()
    private val tvProgram : LiveData<TvProgram> = _tvProgram

    private lateinit var stubList : ArrayList<TvProgram>
    private var filterType = TaskRepository.PROGRAM_TYPE.ALL.id

    init {
        stubList = ArrayList<TvProgram>()
    }

    override fun obeserveList(): LiveData<List<TvProgram>> {
        return tvProgramList
    }

    override fun observerProgram(): LiveData<TvProgram> {
        return tvProgram
    }

    override fun oberverDeleteResult(): LiveData<Boolean> {
        return MutableLiveData<Boolean>(true)
    }

    override suspend fun setupData() {
        if (!stubList.isEmpty()) { return }
        for (i in 1..18) {
            stubList.add(TvProgram("Program" + (i * 10).toString(),
                "Description" + (i * 100).toString(), (i % 2 == 0)))
        }
    }

    override suspend fun loadData(type: Int) {
        Log.d(Global.TAG, "loadData:" + type.toString())
        filterType = type
        var arrayList = ArrayList<TvProgram>()
        if (type == TaskRepository.PROGRAM_TYPE.ALL.id) {
            arrayList = stubList
        } else if (type == TaskRepository.PROGRAM_TYPE.ACTIVE.id) {
            stubList.forEach { tvprogram ->
                if (!tvprogram.isCompleted) {
                    arrayList.add(tvprogram)
                }
            }
        } else if (type == TaskRepository.PROGRAM_TYPE.COMPLETED.id) {
            stubList.forEach { tvprogram ->
                if (tvprogram.isCompleted) {
                    arrayList.add(tvprogram)
                }
            }
        }
        _tvProgramList.value = arrayList
    }

    override fun getBroadID(id: Int): String {
        Log.d(Global.TAG, "getBroadID:" + id.toString())
        return "data_" + id.toString()
    }

    override suspend fun setProgramCopleted(in_program: TvProgram, completed: Boolean) {
        stubList.forEach() { tvProgram ->
            if (in_program.id == tvProgram.id) {
                tvProgram.isCompleted = completed
            }
        }
        loadData(filterType)
    }

    override suspend fun getProgram(id: String) {
        var resultProgram: TvProgram? = null
        run loop@{
            stubList.forEach() { tvProgram ->
                if (tvProgram.id == id) {
                    resultProgram = tvProgram
                    return@loop
                }
            }
        }
        resultProgram ?.let { resultTvProgram ->
            _tvProgram.value = resultTvProgram
        }
    }

    override suspend fun deleteProgram(id: String) {
        run loop@{
            stubList.forEach() { tvProgram ->
                if (tvProgram.id == id) {
                    stubList.remove(tvProgram)
                    return@loop
                }
            }
        }
    }

    override fun addProgramToList(program: TvProgram) {
        stubList.add(program)
    }

    override fun clearProgramList() {
        stubList.clear()
    }
}