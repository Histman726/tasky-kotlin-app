package com.example.tasky.utilitiesEvent

object EventRepository {
    private val eventsList = mutableListOf<Event>()
    private var nextEventId = 1

    fun getAllEvents(): List<Event> = eventsList.toList()

    fun addEvent(event: Event) {
        val newEvent = event.copy(id = nextEventId++)
        eventsList.add(newEvent)
    }

    fun updateEvent(eventId: Int, updatedEvent: Event) {
        val index = eventsList.indexOfFirst { it.id == eventId }
        if (index != -1) {
            eventsList[index] = updatedEvent
        }
    }

    fun deleteEvent(eventId: Int) {
        eventsList.removeAll { it.id == eventId }
    }

    fun getNextEventId(): Int = nextEventId
}