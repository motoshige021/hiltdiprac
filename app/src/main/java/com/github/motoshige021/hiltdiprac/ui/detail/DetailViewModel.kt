package com.github.motoshige021.hiltdiprac.ui.detail

import androidx.lifecycle.*
import com.github.motoshige021.hiltdiprac.TaskRepository
import com.github.motoshige021.hiltdiprac.data.TvProgram
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: TaskRepository)
    :  ViewModel() {
    private var _getProgram = MutableLiveData<String>("")
    private val _program : LiveData<TvProgram> = _getProgram.switchMap { programId ->
        viewModelScope.launch {
            repository.getProgram(programId)
        }
        repository.observerProgram().distinctUntilChanged().switchMap {
            var program = MutableLiveData<TvProgram>()
            program.value = it
            program
        }
    }
    val program: LiveData<TvProgram> = _program

    private var _deleteProgram = MutableLiveData<String>("")
    private val _deleteResult : LiveData<Boolean> = _deleteProgram.switchMap { programId ->
        viewModelScope.launch {
            repository.deleteProgram(programId)
        }
        repository.oberverDeleteResult().distinctUntilChanged().switchMap {
            var result = MutableLiveData<Boolean>(it)
            result
        }
    }

    val deleteResult : LiveData<Boolean> = _deleteResult

    fun getProgram(programId: String) {
        _getProgram.value = programId
    }

    fun deleteProgram(programId: String) {
        _deleteProgram.value = programId
    }
}