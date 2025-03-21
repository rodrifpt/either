package com.cox.poc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cox.domain.model.ResultResponse
import com.cox.domain.usecase.GetUpcomingMoviesUseCase
import com.cox.poc.ui.mapper.LoadingViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Test class of [com.cox.poc.MovieViewModelTest]
 */

@HiltViewModel
class MovieViewModel @Inject constructor(private val useCase: GetUpcomingMoviesUseCase) :
    ViewModel() {
    private val page = 1
    private val _upcomingState = MutableStateFlow(LoadingViewState<List<ResultResponse>>(emptyList()))
    val upcomingState = _upcomingState.asStateFlow()

    fun getUpcoming() {
        viewModelScope.launch {
            val newState = useCase.invoke(page = page)
                .fold({ error ->
                    _upcomingState.value.asFailure(error.userFriendlyMessage)
                }, { result ->
                    _upcomingState.value.asSuccess(result)
                })
            _upcomingState.update { newState }
        }
    }
}
