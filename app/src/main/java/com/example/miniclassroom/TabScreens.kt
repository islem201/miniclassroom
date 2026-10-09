package com.example.miniclassroom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {
    val items = mutableStateListOf(TodoItem(1, "Réviser"), TodoItem(2, "TP", true))

    fun add(t: String) { if (t.isNotBlank()) items.add(0, TodoItem(items.size + 100, t.trim())) }
    fun toggle(item: TodoItem) { items[items.indexOf(item)] = item.copy(done = !item.done) }
    fun remove(item: TodoItem) { items.remove(item) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(vm: TodoViewModel) {
    var task by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("✅ À faire") }) }) { p ->
        Column(Modifier.padding(p)) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(task, { task = it }, Modifier.weight(1f), placeholder = { Text("Nouvelle tâche...") })
                IconButton({ vm.add(task); task = "" }) { Icon(Icons.Default.Add, null) }
            }
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(vm.items, key = { it.id }) { item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(item.done, { vm.toggle(item) })
                        Text(item.title, Modifier.weight(1f), textDecoration = if (item.done) TextDecoration.LineThrough else null)
                        IconButton({ vm.remove(item) }) { Icon(Icons.Default.Delete, null) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionsScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("📤 Mes rendus") }) }) { p ->
        LazyColumn(Modifier.padding(p), contentPadding = PaddingValues(16.dp)) {
            items(fakeSubmissions) { s ->
                Card(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(s.title, style = MaterialTheme.typography.titleMedium)
                        Text("${s.subject} · ${s.dueDate}", style = MaterialTheme.typography.bodyMedium)
                        Text(s.status, color = if (s.status == "Rendu") Color(0xFF2E7D32) else Color.Red)
                    }
                }
            }
        }
    }
}