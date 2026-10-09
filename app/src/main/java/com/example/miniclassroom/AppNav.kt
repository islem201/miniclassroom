package com.example.miniclassroom

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val todoVm: TodoViewModel = viewModel()
    val current = nav.currentBackStackEntryAsState().value?.destination?.route

    val tabs = listOf(
        Triple("subjects", "Cours", Icons.Default.Home),
        Triple("todo", "À faire", Icons.Default.CheckCircle),
        Triple("submissions", "Rendus", Icons.AutoMirrored.Filled.Send)
    )

    Scaffold(bottomBar = {
        NavigationBar {
            tabs.forEach { (route, label, icon) ->
                NavigationBarItem(
                    selected = current?.startsWith(route) == true,
                    onClick = { nav.navigate(route) { popUpTo("subjects"); launchSingleTop = true } },
                    icon = { Icon(icon, contentDescription = label) },
                    label = { Text(label) }
                )
            }
        }
    }) { padding ->
        NavHost(nav, startDestination = "subjects", modifier = Modifier.padding(padding)) {

            composable("subjects") {
                SubjectsScreen(
                    onSubjectClick = { nav.navigate("subjects/$it") },
                    onCourseClick = { nav.navigate("subjects/course/$it") }
                )
            }
            composable(
                "subjects/{subjectId}",
                arguments = listOf(navArgument("subjectId") { type = NavType.IntType })
            ) {
                CoursesScreen(
                    subjectId = it.arguments!!.getInt("subjectId"),
                    onCourseClick = { id -> nav.navigate("subjects/course/$id") },
                    onBack = { nav.popBackStack() }
                )
            }
            composable(
                "subjects/course/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) {
                ContentScreen(it.arguments!!.getInt("courseId"), onBack = { nav.popBackStack() })
            }

            composable("todo") { TodoScreen(todoVm) }
            composable("submissions") { SubmissionsScreen() }
        }
    }
}