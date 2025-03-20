package com.cox.poc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cox.domain.model.ResultResponse
import com.cox.domain.repository.IMovieRepository
import com.cox.poc.ui.mapper.LoadingViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: IMovieRepository) : ViewModel() {
    private val page = 1

    private val _upcomingState = MutableStateFlow(LoadingViewState<List<ResultResponse>>(emptyList()))
    val upcomingState = _upcomingState.asStateFlow()

    fun getUpcoming() {
        viewModelScope.launch {
            val newState = repository.getUpcoming(page = page)
                .fold({
                    _upcomingState.value.asFailure()
                }, { model ->
                    _upcomingState.value.asSuccess(model.results)
                })
            _upcomingState.update { newState }
        }
    }
}
