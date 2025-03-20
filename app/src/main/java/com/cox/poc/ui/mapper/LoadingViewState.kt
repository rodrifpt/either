package com.cox.poc.ui.mapper

data class LoadingViewState<T>(
    val data: T,
    val loadType: LoadType = LoadType.Load,
    val failed: Boolean = false,
    val errorMessage: String? = null
) {

    val isLoading
        get() = loadType == LoadType.Load

    fun asFailure(errorMessage: String?) = copy(
        loadType = LoadType.Idle,
        failed = true,
        errorMessage = errorMessage
    )

    fun asSuccess(input: T) = copy(
        loadType = LoadType.Idle,
        failed = false,
        errorMessage = null,
        data = input
    )

    enum class LoadType {
        Idle,
        Load
    }
}
