package com.github.motoshige021.hiltdiprac.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.motoshige021.hiltdiprac.FakeTaskRepository
import com.github.motoshige021.hiltdiprac.MainCoroutineRule
import com.github.motoshige021.hiltdiprac.data.TvProgram
import com.github.motoshige021.hiltdiprac.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    @get:Rule
    var instExecutorRule = InstantTaskExecutorRule()

    lateinit var fakeRepository : FakeTaskRepository
    lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeTaskRepository()
        detailViewModel = DetailViewModel(fakeRepository)
    }

    fun addTestData() {
        fakeRepository.addProgramToList(TvProgram("title1", "desc1", true, "id-1"))
        fakeRepository.addProgramToList(TvProgram("title2", "desc2", false, "id-2"))
        fakeRepository.addProgramToList(TvProgram("title3", "desc3", true, "id-3"))
    }

    @Test
    fun getProgramTest() {
        fakeRepository.clearProgramList()
        addTestData()
        detailViewModel.getProgram("id-2")
        detailViewModel.program.observeForTesting {
            assertTrue(detailViewModel.program.value!!.id == "id-2")
        }
    }

    @Test
    fun deleteProgramTest() {
        fakeRepository.clearProgramList()
        addTestData()
        var programId = "id-3"
        detailViewModel.deleteProgram(programId)
        detailViewModel.deleteResult.observeForTesting {
            detailViewModel.deleteResult.value?.let { result ->
                assertTrue(result)
            }
        }
        var isExist = fakeRepository.checkExistProgram(programId)
        assertFalse(isExist)

        detailViewModel.deleteProgram(programId)
        detailViewModel.deleteResult.observeForTesting {
            detailViewModel.deleteResult.value?.let { result ->
                assertFalse(result)
            }
        }
    }
}