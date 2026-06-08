package com.example.myapplication.ui.presentation.grades

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.AddGradeUseCase
import com.example.myapplication.domain.usecase.GetGradesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GradeViewModel(
    private val getGradesUseCase: GetGradesUseCase,
    private val addGradeUseCase: AddGradeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GradeUiState>(GradeUiState.Loading)
    val uiState: StateFlow<GradeUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    var currentScreen by mutableStateOf(GradeScreenType.LIST)
        private set

    var newActivityName by mutableStateOf("")
        private set
    var newSubject by mutableStateOf("")
        private set
    var newGradeText by mutableStateOf("")
        private set

    init {
        observeGrades()
    }



    private fun observeGrades() {
        viewModelScope.launch {
            try {
                getGradesUseCase().collect { gradeList ->
                    val average = if (gradeList.isEmpty()) 0.0
                    else gradeList.map { it.grade }.average()
                    _uiState.value = GradeUiState.Success(
                        grades = gradeList,
                        average = average
                    )
                }
            } catch (e: Exception) {
                _uiState.value = GradeUiState.Error("Error al cargar: ${e.localizedMessage}")
            }
        }
    }

    fun onActivityNameChange(value: String) { newActivityName = value }
    fun onSubjectChange(value: String) { newSubject = value }
    fun onGradeTextChange(value: String) { newGradeText = value }

    fun onNavigateToCreate() {
        newActivityName = ""
        newSubject = ""
        newGradeText = ""
        _errorMessage.value = null
        currentScreen = GradeScreenType.CREATE
    }



    fun onNavigateToList() {
        _errorMessage.value = null
        currentScreen = GradeScreenType.LIST
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }

    fun onSaveGrade() {
        // Validación alfabética / no numérica
        val gradeValue = newGradeText.toDoubleOrNull()
        if (gradeValue == null) {
            _errorMessage.value = "La nota ingresada no es un número válido."
            return
        }

        // Validación de rango 0.0 - 10.0
        if (gradeValue < 0.0 || gradeValue > 10.0) {
            _errorMessage.value = "La nota debe estar entre 0.0 y 10.0."
            return
        }

        if (newActivityName.isBlank()) {
            _errorMessage.value = "El nombre de la actividad no puede estar vacío."
            return
        }

        if (newSubject.isBlank()) {
            _errorMessage.value = "La materia no puede estar vacía."
            return
        }

        viewModelScope.launch {
            try {
                addGradeUseCase(newActivityName, newSubject, gradeValue)
                _errorMessage.value = null
                currentScreen = GradeScreenType.LIST
            } catch (e: IllegalArgumentException) {
                _errorMessage.value = e.message ?: "Dato inválido."
            } catch (e: Exception) {
                _errorMessage.value = "Error al guardar: ${e.localizedMessage}"
            }
        }
    }
}