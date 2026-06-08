package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.repository.AcademicTaskRepository

class AddTaskUseCase(private val repository: AcademicTaskRepository) {
    suspend operator fun invoke(title: String) {
        if (title.trim().length < 5) {
            throw IllegalArgumentException("La regla de dominio exige un minimo de 5 caracteres.")
        }
        repository.addTask(title.trim())
    }
}
