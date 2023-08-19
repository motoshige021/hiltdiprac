package com.github.motoshige021.hiltdiprac.ui.main

import androidx.lifecycle.*
import com.github.motoshige021.hiltdiprac.TaskRepository
import com.github.motoshige021.hiltdiprac.data.TvProgram
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.github.motoshige021.hiltdiprac.R
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: TaskRepository)
    : ViewModel() {
    //fun loadData(id: Int) { repository.loadData(id) }

    private var _update = MutableLiveData<Boolean>(false)
    private var _getProgram = MutableLiveData<Boolean>(false)
    var getProgramId : String = ""
    //var update : LiveData<Boolean> = _update

    private val _items : LiveData<List<TvProgram>> = _update.switchMap { update ->
        if (update) {
            viewModelScope.launch {
                repository.loadData(filter.id)
            }
        }
        repository.obeserveList().distinctUntilChanged().switchMap {
            var listData = MutableLiveData<List<TvProgram>>()
            listData.value = it
            listData
        }
        //var listData = MutableLiveData<List<TvProgram>>()
        //listData.value = repository.loadData(filter.id)
        //listData
    }
    val items : LiveData<List<TvProgram>> = _items

    private val _program : LiveData<TvProgram> = _getProgram.switchMap {
        viewModelScope.launch {
            repository.getProgram(getProgramId)
        }

        repository.observerProgram().distinctUntilChanged().switchMap {
            var program = MutableLiveData<TvProgram>()
            program.value = it
            program
        }
    }
    val tvProgram : LiveData<TvProgram> = _program

    private val _regionCode = MutableLiveData<Int>()
    val regionCode : LiveData<Int> = _regionCode

    //private val _showProgramId = MutableLiveData<String>()
    //val showProgramId : LiveData<String> = _showProgramId

    private var filter = TaskRepository.PROGRAM_TYPE.ALL

    init {
        //_items.value = repository.loadData(TaskRepository.PROGRAM_TYPE.ALL.id)
        viewModelScope.launch {
            repository.setupData()
            _update.value = true
        }
        _regionCode.value = R.string.region2
    }

    fun getProgram(id: String)  {
        getProgramId = id
        _getProgram.value = true   //repository.getProgram(id)
    }
    fun getBroadID(id: Int) : String { return repository.getBroadID(id) }
    fun setFiltering(in_filter: TaskRepository.PROGRAM_TYPE) {
        filter = in_filter
        _update.value = true
    }

    fun completedProgram(program: TvProgram, completed: Boolean) {
        viewModelScope.launch {
            repository.setProgramCopleted(program, completed)
            _update.value = true
        }
    }

    fun showProgramDetail(id: String) {
        getProgram(id)
    }
}