package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.AcademicGrade
import com.example.myapplication.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow

class GetGradesUseCase(private val repository: GradeRepository) {
    operator fun invoke(): Flow<List<AcademicGrade>> {
        return repository.getGradesStream()
    }
}