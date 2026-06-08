package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.AcademicGrade
import kotlinx.coroutines.flow.Flow

interface GradeRepository {
    fun getGradesStream(): Flow<List<AcademicGrade>>
    suspend fun addGrade(activityName: String, subject: String, grade: Double)
}