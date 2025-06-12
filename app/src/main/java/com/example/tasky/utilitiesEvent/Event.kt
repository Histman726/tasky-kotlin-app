package com.example.tasky.utilitiesEvent

data class Event(
    val id: Int,
    val day: Int,
    val month: String,
    val eventType: String,
    val note: String
)