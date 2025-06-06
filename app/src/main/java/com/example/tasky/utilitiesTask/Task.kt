package com.example.tasky.utilitiesTask

data class Task(val name: String, val category: String, val date: String, val time: String, val description: String, val priority: String, var isSelected: Boolean = false) {
}
