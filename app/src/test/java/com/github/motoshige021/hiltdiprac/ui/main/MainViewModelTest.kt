package com.github.motoshige021.hiltdiprac.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.motoshige021.hiltdiprac.*
import com.github.motoshige021.hiltdiprac.data.TvProgram
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instExecutorRule = InstantTaskExecutorRule()

    lateinit var fakeRepository : FakeTaskRepository
    lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeTaskRepository()
        mainViewModel = MainViewModel(fakeRepository)
    }

    fun addTestData() {
        fakeRepository.addProgramToList(TvProgram("title1", "desc1", true, "id-1"))
        fakeRepository.addProgramToList(TvProgram("title2", "desc2", false, "id-2"))
        fakeRepository.addProgramToList(TvProgram("title3", "desc3", true, "id-3"))
    }

    @Test
    fun getAllProgramGTest() {
        fakeRepository.clearProgramList()
        addTestData()

        mainViewModel.setFiltering(TaskRepository.PROGRAM_TYPE.ALL)
        
        mainViewModel.items.observeForTesting {

            assertTrue(mainViewModel.items.value!!.size == 3)
        }
    }

    @Test
    fun getActiveProgramTest() {
        fakeRepository.clearProgramList()
        addTestData()

        mainViewModel.setFiltering(TaskRepository.PROGRAM_TYPE.ACTIVE)
        mainViewModel.items.observeForTesting {

            assertTrue(mainViewModel.items.value!!.size == 1)

            var program = mainViewModel.items.value!!.get(0)
            assertTrue(program.id == "id-2")
        }
    }

    @Test
    fun getCompletedProgramTest() {
        fakeRepository.clearProgramList()
        addTestData()
        mainViewModel.setFiltering(TaskRepository.PROGRAM_TYPE.COMPLETED)

        mainViewModel.items.observeForTesting {

            assertTrue(mainViewModel.items.value!!.size == 2)
            var program = mainViewModel.items.value!!.get(0)
            assertTrue(program.id == "id-1")
            var program2 = mainViewModel.items.value!!.get(1)
            assertTrue(program2.id == "id-3")
        }
    }

    @Test
    fun setProgramCompletedTest() {
        fakeRepository.clearProgramList()
        addTestData()
        fakeRepository.setFilterType(TaskRepository.PROGRAM_TYPE.ALL.id)

        var program = TvProgram("title1", "desc1", true, "id-1")
        mainViewModel.completedProgram(program, false)

        mainViewModel.items.observeForTesting {

            var program = mainViewModel.items.value!!.get(0)
            assertTrue(program.id == "id-1")
            assertFalse(program.isCompleted)
        }
    }

    @Test
    fun getOneProgramTest() {
        fakeRepository.clearProgramList()
        fakeRepository.addProgramToList(TvProgram("title1", "desc1", true, "id-1"))
        mainViewModel.setFiltering(TaskRepository.PROGRAM_TYPE.ALL)
        mainViewModel.items.observeForTesting {
            assertTrue(mainViewModel.items.value!!.size == 1)

            var program = mainViewModel.items.value!!.get(0)
            assertTrue(program.id == "id-1")
            assertTrue(program.isCompleted)
            assertTrue(program.title == "title1")
            assertTrue(program.description == "desc1")
        }
    }

    @Test
    fun getNoProgramTest() {
        fakeRepository.clearProgramList()
        mainViewModel.setFiltering(TaskRepository.PROGRAM_TYPE.ALL)
        mainViewModel.items.observeForTesting {
            assertTrue(mainViewModel.items.value!!.size == 0)
        }
    }

    @Test
    fun getBroadIdTest() {
        assertTrue(mainViewModel.getBroadID(20) == "FakeID-20")
    }

    @Test
    fun regionCodeTest() {
        assertTrue(mainViewModel.regionCode.value == R.string.region2)
    }
}