package com.example.tasky

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasky.utilitiesEvent.Event
import com.example.tasky.utilitiesEvent.EventAdapter
import com.example.tasky.utilitiesEvent.EventRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CalendarActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var cardList: CardView
    private val eventsList = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        setupRecyclerView()
        setupFab()
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
            { event, position -> showEditEventDialog(event, position) },
            { event, position -> showDeleteConfirmation(event, position) }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CalendarActivity)
            adapter = eventAdapter
        }
    }

    private fun setupFab() {
        fab = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            showAddEventDialog()
        }
    }

    private fun setupNavigationButtons() {
        cardList = findViewById(R.id.card_list)

        cardList.setOnClickListener {
            val intent = Intent(this, AgendaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showAddEventDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_event, null)

        val etDay = dialogView.findViewById<EditText>(R.id.etDay)
        val etMonth = dialogView.findViewById<EditText>(R.id.etMonth)
        val etEventType = dialogView.findViewById<EditText>(R.id.etEventType)
        val etNote = dialogView.findViewById<EditText>(R.id.etNote)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnSave.setOnClickListener {
            val dayText = etDay.text.toString().trim()
            val month = etMonth.text.toString().trim()
            val eventType = etEventType.text.toString().trim()
            val note = etNote.text.toString().trim()

            if (validateInput(dayText, month, eventType)) {
                val day = dayText.toInt()
                val newEvent = Event(0, day, month, eventType, note) // ID será asignado por el repository
                EventRepository.addEvent(newEvent)
                loadEventsFromRepository() // Recargar datos
                dialog.dismiss()
                Toast.makeText(this, "Evento agregado correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun validateInput(day: String, month: String, eventType: String): Boolean {
        if (day.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa el día", Toast.LENGTH_SHORT).show()
            return false
        }

        val dayInt = day.toIntOrNull()
        if (dayInt == null || dayInt < 1 || dayInt > 31) {
            Toast.makeText(this, "Por favor ingresa un día válido (1-31)", Toast.LENGTH_SHORT).show()
            return false
        }

        if (month.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa el mes", Toast.LENGTH_SHORT).show()
            return false
        }

        if (eventType.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa el tipo de evento", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun showEventDetails(event: Event) {
        val message = "Día: ${event.day}\n" +
                "Mes: ${event.month}\n" +
                "Evento: ${event.eventType}\n" +
                "Nota: ${if (event.note.isEmpty()) "Sin notas" else event.note}"

        AlertDialog.Builder(this)
            .setTitle("Detalles del Evento")
            .setMessage(message)
            .setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditEventDialog(event: Event, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_event, null)

        val etDay = dialogView.findViewById<EditText>(R.id.etDay)
        val etMonth = dialogView.findViewById<EditText>(R.id.etMonth)
        val etEventType = dialogView.findViewById<EditText>(R.id.etEventType)
        val etNote = dialogView.findViewById<EditText>(R.id.etNote)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        // Prellenar campos con datos actuales
        etDay.setText(event.day.toString())
        etMonth.setText(event.month)
        etEventType.setText(event.eventType)
        etNote.setText(event.note)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Editar Evento")
            .create()

        btnSave.setOnClickListener {
            val dayText = etDay.text.toString().trim()
            val month = etMonth.text.toString().trim()
            val eventType = etEventType.text.toString().trim()
            val note = etNote.text.toString().trim()

            if (validateInput(dayText, month, eventType)) {
                try {
                    val day = dayText.toInt()
                    val updatedEvent = Event(event.id, day, month, eventType, note)
                    EventRepository.updateEvent(event.id, updatedEvent)
                    loadEventsFromRepository() // Recargar datos
                    dialog.dismiss()
                    Toast.makeText(this, "Evento actualizado correctamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al actualizar evento", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteConfirmation(event: Event, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Evento")
            .setMessage("¿Estás seguro de que quieres eliminar este evento?\n\n${event.eventType} - ${event.day} de ${event.month}")
            .setPositiveButton("Eliminar") { _, _ ->
                try {
                    EventRepository.deleteEvent(event.id)
                    loadEventsFromRepository() // Recargar datos
                    Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al eliminar evento", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}