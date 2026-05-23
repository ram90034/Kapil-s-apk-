package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.ExerciseData
import com.example.data.WorkoutRepository
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ExerciseDetailScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.DietScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.WorkoutViewModel
import com.example.viewmodel.WorkoutViewModelFactory

sealed class NavigationTab(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Workouts : NavigationTab("workouts", "Workouts", Icons.Default.FitnessCenter)
    object Diet : NavigationTab("diet", "Diet Bot", Icons.Default.Restaurant)
    object Progress : NavigationTab("progress", "Progress", Icons.Default.ShowChart)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Core Database and Repository initialization
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = WorkoutRepository(database.workoutLogDao())
        
        // Simple Constructor Injection factory setup
        val factory = WorkoutViewModelFactory(repository, applicationContext)
        val viewModel = ViewModelProvider(this, factory)[WorkoutViewModel::class.java]

        setContent {
            MyApplicationTheme {
                GymAppContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GymAppContainer(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf<NavigationTab>(NavigationTab.Workouts) }
    var selectedExerciseId by remember { mutableStateOf<String?>(null) }

    val tabs = listOf(NavigationTab.Workouts, NavigationTab.Diet, NavigationTab.Progress)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            // Only show bottom navigation if NOT viewing exercise detail full screen (for optimal focus!)
            if (selectedExerciseId == null) {
                NavigationBar(
                    modifier = Modifier
                        .testTag("bottom_nav_bar")
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    tonalElevation = 8.dp
                ) {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = activeTab == tab,
                            onClick = { activeTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title
                                )
                            },
                            label = { Text(tab.title) },
                            modifier = Modifier.testTag("nav_tab_${tab.route}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main multi-screen content controller
            when (activeTab) {
                NavigationTab.Workouts -> {
                    DashboardScreen(
                        viewModel = viewModel,
                        onExerciseSelect = { id -> selectedExerciseId = id }
                    )
                }
                NavigationTab.Diet -> {
                    DietScreen(
                        viewModel = viewModel
                    )
                }
                NavigationTab.Progress -> {
                    ProgressScreen(
                        viewModel = viewModel
                    )
                }
            }

            // Slide overlay for detailed movement visuals with backstack support
            AnimatedVisibility(
                visible = selectedExerciseId != null,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                selectedExerciseId?.let { id ->
                    val exercise = remember(id) { ExerciseData.getExerciseById(id) }
                    if (exercise != null) {
                        ExerciseDetailScreen(
                            exercise = exercise,
                            viewModel = viewModel,
                            onBack = { selectedExerciseId = null }
                        )
                    }
                }
            }
        }
    }
}
