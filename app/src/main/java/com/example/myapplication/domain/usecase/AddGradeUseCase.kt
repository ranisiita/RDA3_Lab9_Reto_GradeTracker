package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.repository.GradeRepository

class AddGradeUseCase(private val repository: GradeRepository) {

    suspend operator fun invoke(
        activityName: String,
        subject: String,
        grade: Double
    ) {
        //Regla 1: campos vacíos prohibidos
        if (activityName.trim().isBlank() || subject.trim().isBlank()) {
            throw IllegalArgumentException("Los campos de actividad y asignatura no pueden estar vacíos.")
        }
        //Regla 2: nota entre 0.0 y 10.0
        if (grade < 0.0 || grade > 10.0) {
            throw IllegalArgumentException("La nota debe estar en el rango de 0.0 a 10.0. Valor recibido: $grade")
        }
        repository.addGrade(activityName.trim(), subject.trim(), grade)
    }
}


