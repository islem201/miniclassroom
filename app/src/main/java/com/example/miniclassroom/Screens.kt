package com.example.miniclassroom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Page(title: String, onBack: (() -> Unit)? = null, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (onBack != null) IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour")
                    }
                }
            )
        },
        content = content
    )
}

@Composable
fun ClickableCard(text: String, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(text, Modifier.padding(20.dp), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun SubjectsScreen(onSubjectClick: (Int) -> Unit, onCourseClick: (Int) -> Unit) {
    var query by remember { mutableStateOf("") }
    val results = FakeData.courses.filter { it.title.contains(query, ignoreCase = true) }

    Page("🎓 Mes cours") { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Rechercher un cours") },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                contentPadding = PaddingValues(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (query.isBlank()) {
                    items(FakeData.subjects) { ClickableCard("${it.emoji} ${it.name}") { onSubjectClick(it.id) } }
                } else {
                    items(results) { ClickableCard(it.title) { onCourseClick(it.id) } }
                }
            }
        }
    }
}

@Composable
fun CoursesScreen(subjectId: Int, onCourseClick: (Int) -> Unit, onBack: () -> Unit) {
    val subject = FakeData.subjects.first { it.id == subjectId }

    Page(subject.name, onBack) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(FakeData.courses.filter { it.subjectId == subjectId }) {
                ClickableCard(it.title) { onCourseClick(it.id) }
            }
        }
    }
}

@Composable
fun ContentScreen(courseId: Int, onBack: () -> Unit, vm: SummaryViewModel = viewModel()) {
    val course = FakeData.courses.first { it.id == courseId }

    Page(course.title, onBack) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(course.content, style = MaterialTheme.typography.bodyLarge)

            Button(
                onClick = { vm.summarize(course.content) },
                enabled = vm.state !is SummaryState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) { Text("✨ Résumer") }

            when (val s = vm.state) {
                SummaryState.Idle -> {}
                SummaryState.Loading -> CircularProgressIndicator()
                is SummaryState.Success -> Card(Modifier.fillMaxWidth()) { Text(s.text, Modifier.padding(16.dp)) }
                is SummaryState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}