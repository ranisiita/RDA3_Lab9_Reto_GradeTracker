package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.AcademicTask
import com.example.myapplication.domain.repository.AcademicTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID

class InMemoryTaskRepository : AcademicTaskRepository {

    // Simulacion de una base de datos en memoria mediante un flujo mutable reactivo
    private val tasksFlow = MutableStateFlow<List<AcademicTask>>(
        listOf(
            AcademicTask(UUID.randomUUID().toString(), "Estudiar Patron MVVM", false),
            AcademicTask(UUID.randomUUID().toString(), "Analizar principios de Clean Architecture", true)
        )
    )

    override fun getTasksStream(): Flow<List<AcademicTask>> = tasksFlow

    override suspend fun addTask(title: String) {
        val newTask = AcademicTask(id = UUID.randomUUID().toString(), title = title, isCompleted = false)
        tasksFlow.value = tasksFlow.value + newTask
    }

    override suspend fun toggleTaskCompletion(taskId: String) {
        tasksFlow.value = tasksFlow.value.map { task ->
            if (task.id == taskId) {
                task.copy(isCompleted = !task.isCompleted)
            } else {
                task
            }
        }
    }
}
