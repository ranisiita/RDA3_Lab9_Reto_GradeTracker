package com.example.myapplication.ui.presentation.tasks

import com.example.myapplication.domain.model.AcademicTask

sealed class AcademicTaskUiState {
    object Loading : AcademicTaskUiState()
    data class Success(val tasks: List<AcademicTask>) : AcademicTaskUiState()
    data class Error(val message: String) : AcademicTaskUiState()
}

enum class ScreenType {
    LIST, CREATE
}
