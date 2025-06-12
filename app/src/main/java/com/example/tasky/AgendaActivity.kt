package com.example.tasky

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasky.utilitiesEvent.Event
import com.example.tasky.utilitiesEvent.EventAdapter
import com.example.tasky.utilitiesEvent.EventRepository

class AgendaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var cardCalendar: CardView
    private val eventsList = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)

        setupRecyclerView()
        setupNavigationButtons()
        loadEventsFromRepository()
    }

    override fun onResume() {
        super.onResume()
        loadEventsFromRepository()
    }

    private fun loadEventsFromRepository() {
        eventsList.clear()
        eventsList.addAll(EventRepository.getAllEvents())
        eventAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewEvents)
        eventAdapter = EventAdapter(
            eventsList,
            { event -> showEventDetails(event) },
            { _, _ -> }, // Sin edición en esta pantalla
            { _, _ -> }  // Sin eliminación en esta pantalla
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AgendaActivity)
            adapter = eventAdapter
        }
    }

    private fun setupNavigationButtons() {
        cardCalendar = findViewById(R.id.card_calendar)

        // Click en calendario para regresar a MainActivity
        cardCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish() // Opcional: cerrar esta actividad
        }
    }

    private fun showEventDetails(event: Event) {
        val message = "Día: ${event.day}\n" +
                "Mes: ${event.month}\n" +
                "Evento: ${event.eventType}\n" +
                "Nota: ${if (event.note.isEmpty()) "Sin notas" else event.note}"

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Detalles del Evento")
            .setMessage(message)
            .setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}