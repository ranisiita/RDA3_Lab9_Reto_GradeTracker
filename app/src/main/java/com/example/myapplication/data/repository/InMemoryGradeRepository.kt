package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.AcademicGrade
import com.example.myapplication.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class InMemoryGradeRepository : GradeRepository {

    private val gradesFlow = MutableStateFlow<List<AcademicGrade>>(
        listOf(
            AcademicGrade(UUID.randomUUID().toString(), "Parcial 1", "Arquitectura de Software", 8.5),
            AcademicGrade(UUID.randomUUID().toString(), "Tarea 1", "Desarrollo Móvil", 9.0)
        )
    )

    override fun getGradesStream(): Flow<List<AcademicGrade>> = gradesFlow

    override suspend fun addGrade(activityName: String, subject: String, grade: Double) {
        val newGrade = AcademicGrade(
            id = UUID.randomUUID().toString(),
            activityName = activityName,
            subject = subject,
            grade = grade
        )
        gradesFlow.value = gradesFlow.value + newGrade
    }
}


