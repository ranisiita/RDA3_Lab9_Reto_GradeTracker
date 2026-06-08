package com.example.myapplication.ui.presentation.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.AcademicTask

@Composable
fun AcademicTaskApp(viewModel: AcademicTaskViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (viewModel.currentScreen) {
            ScreenType.LIST -> AcademicTaskListScreen(viewModel)
            ScreenType.CREATE -> AcademicTaskCreateScreen(viewModel)
        }
    }
}

@Composable
fun AcademicTaskListScreen(viewModel: AcademicTaskViewModel) {
    // Suscripcion al flujo inmutable que dispara recomposiciones inteligentes
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onNavigateToCreate() }) {
                Text(text = "+", style = MaterialTheme.typography.titleLarge)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is AcademicTaskUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is AcademicTaskUiState.Success -> {
                    if (state.tasks.isEmpty()) {
                        Text(text = "No tienes tareas registradas.", style = MaterialTheme.typography.bodyLarge)
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(state.tasks) { task ->
                                AcademicTaskRow(
                                    task = task,
                                    onCheckedChange = { viewModel.onTaskCheckedChange(task.id) }
                                )
                            }
                        }
                    }
                }
                is AcademicTaskUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = { viewModel.onNavigateToCreate() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AcademicTaskCreateScreen(viewModel: AcademicTaskViewModel) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nueva Tarea Academica",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = viewModel.newTaskTitle,
                onValueChange = { viewModel.onTaskTitleChange(it) },
                label = { Text("Titulo descriptivo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = { viewModel.onNavigateToList() }) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { viewModel.onSaveTask() },
                    enabled = viewModel.newTaskTitle.isNotBlank()
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

@Composable
fun AcademicTaskRow(task: AcademicTask, onCheckedChange: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { _ -> onCheckedChange() }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
