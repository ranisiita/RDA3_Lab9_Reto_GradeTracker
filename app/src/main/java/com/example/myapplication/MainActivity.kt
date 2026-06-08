package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.InMemoryGradeRepository
import com.example.myapplication.domain.usecase.AddGradeUseCase
import com.example.myapplication.domain.usecase.GetGradesUseCase
import com.example.myapplication.ui.presentation.grades.GradeTrackerApp
import com.example.myapplication.ui.presentation.grades.GradeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val vm: GradeViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val repo = InMemoryGradeRepository()
                        @Suppress("UNCHECKED_CAST")
                        return GradeViewModel(
                            GetGradesUseCase(repo),
                            AddGradeUseCase(repo)
                        ) as T
                    }
                }
            )
            GradeTrackerApp(viewModel = vm)
        }
    }
}