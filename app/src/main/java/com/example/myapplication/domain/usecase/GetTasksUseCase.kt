package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.AcademicTask
import com.example.myapplication.domain.repository.AcademicTaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(private val repository: AcademicTaskRepository) {
    operator fun invoke(): Flow<List<AcademicTask>> {
        return repository.getTasksStream()
    }
}
