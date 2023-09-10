package com.github.motoshige021.hiltdiprac.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.github.motoshige021.hiltdiprac.LocaldbTaskRepository
import com.github.motoshige021.hiltdiprac.MainCoroutineRule
import com.github.motoshige021.hiltdiprac.TaskRepository
import com.github.motoshige021.hiltdiprac.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LocaldbTaskRepositoryTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    @get:Rule
    var instExecutorRule = InstantTaskExecutorRule()

    lateinit var localdbTaskRepository: LocaldbTaskRepository
    lateinit var dataSource : FakeDataSource

    @Before
    fun setup() {
        dataSource = FakeDataSource()
        localdbTaskRepository = LocaldbTaskRepository(dataSource)
    }

    private fun setTestData() {
        dataSource.addTestData(TvProgram("title1", "desc1", true, "id-1"))
        dataSource.addTestData(TvProgram("title2", "desc2", false, "id-2"))
        dataSource.addTestData(TvProgram("title3", "desc3", true, "id-3"))
    }


    @Test
    fun getAllProgramTest() {
        dataSource.clearProgramList()
        setTestData()
        mainCoroutineRule.runTest {
            localdbTaskRepository.loadData(TaskRepository.PROGRAM_TYPE.ALL.id)
            localdbTaskRepository.obeserveList().distinctUntilChanged().map {
                assert(it.size == 3)
                it
            }
        }
    }

    @Test
    fun getProgramTest() {
        var id = "id-3"
        dataSource.clearProgramList()
        setTestData()
        mainCoroutineRule.runTest {
            localdbTaskRepository.getProgram(id)
            localdbTaskRepository.observerProgram().distinctUntilChanged().map {
                assert(it.id == id)
                assert(it.title == "title3")
                assert(it.description == "desc1")
                assertTrue(it.isCompleted)
                it
            }
        }
    }

    @Test
    fun loadDataTest() {
        dataSource.clearProgramList()
        setTestData()
        mainCoroutineRule.runTest {
            localdbTaskRepository.loadData(TaskRepository.PROGRAM_TYPE.COMPLETED.id)
            localdbTaskRepository.obeserveList().distinctUntilChanged().map {
                assert(it.size == 2)
                assert(it[0].id == "id-1")
                assert(it[1].id == "id-3")
            }
        }
    }

    @Test
    fun loadDataTest2() {
        dataSource.clearProgramList()
        setTestData()
        mainCoroutineRule.runTest {
            localdbTaskRepository.loadData(TaskRepository.PROGRAM_TYPE.ACTIVE.id)
            localdbTaskRepository.obeserveList().distinctUntilChanged().map {
                assert(it.size == 1)
                assert(it[0].id == "id-2")
            }
        }
    }

    @Test
    fun updateTest() {
        dataSource.clearProgramList()
        setTestData()
        mainCoroutineRule.runTest {
            localdbTaskRepository.setProgramCopleted(TvProgram("title1", "desc1", true, "id-1"), false)
            localdbTaskRepository.getProgram("id-1")
            localdbTaskRepository.observerProgram().distinctUntilChanged().map {
                assertFalse(it.isCompleted)
            }
        }
    }

    @Test
    fun deleteTest() {
        var id = "id-1"
        dataSource.clearProgramList()
        setTestData()
        mainCoroutineRule.runTest {
            localdbTaskRepository.deleteProgram(id)
            localdbTaskRepository.oberverDeleteResult().distinctUntilChanged().map {
                assertTrue(it)
                assert(dataSource.getListSize() == 2)
                assertFalse(dataSource.checkExistProgram(id))
            }
        }
    }
}