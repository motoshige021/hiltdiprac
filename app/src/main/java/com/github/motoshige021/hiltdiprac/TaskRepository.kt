package com.github.motoshige021.hiltdiprac

import androidx.lifecycle.LiveData
import com.github.motoshige021.hiltdiprac.data.TvProgram

interface TaskRepository {
    /* DAO対応前、Stubのみの時の実装
    fun loadData(type: Int): List<TvProgram>
    fun setProgramCopleted(program: TvProgram, completed: Boolean)
    fun getProgram(id: String ) : TvProgram?
     */
    fun loadData(type: Int)
    fun setProgramCopleted(program: TvProgram, completed: Boolean)
    fun getProgram(id: String )
    fun setupData()
    fun getBroadID(id: Int): String
    fun obeserveList(): LiveData<List<TvProgram>>
    fun observerProgram(): LiveData<TvProgram>

    enum class PROGRAM_TYPE(val id: Int) {
        ALL(0),
        ACTIVE(1),
        COMPLETED(2),
    }
}
