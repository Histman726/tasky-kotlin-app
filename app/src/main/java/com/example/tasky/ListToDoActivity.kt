package com.example.tasky

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasky.utilitiesTask.Task
import com.example.tasky.utilitiesTask.TasksAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListToDoActivity : AppCompatActivity() {
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var rvTasks: RecyclerView
    private lateinit var btnAddTask: FloatingActionButton
    private lateinit var tvSeccionName: TextView
    private lateinit var tvFecha: TextView
    private lateinit var cvEscuela: CardView
    private lateinit var cvTrabajo: CardView
    private lateinit var cvPersonal: CardView
    private lateinit var fmCalendar: FrameLayout
    private lateinit var fmAgenda: FrameLayout

    //TODO: Obtener las tareas de la base de datos
    private val tasks = mutableListOf<Task>(
        Task("Hacer la tarea", "Escuela", "Hoy", "10:00", "Descripción de la tarea", "Urgente"),
        Task("Hacer el excel", "Laboral", "Hoy", "11:00", "Descripción de la tarea", "Media"),
        Task("Hacer la limpieza", "Personal", "Hoy", "12:00", "Descripción de la tarea", "Baja"),
    )

    private val categoriasSeleccionadas = mutableListOf<Boolean>(false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_to_do)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initListeners()
        initUI()
    }

    private fun initComponents() {
        rvTasks = findViewById(R.id.rvTasks)
        btnAddTask = findViewById(R.id.btnAddTask)
        tvSeccionName = findViewById(R.id.tvSeccionName)
        tvFecha = findViewById(R.id.tvFecha)
        cvEscuela = findViewById(R.id.card_escuela)
        cvTrabajo = findViewById(R.id.card_trabajo)
        cvPersonal = findViewById(R.id.card_personal)
        fmCalendar = findViewById(R.id.fmCalendar)
        fmAgenda = findViewById(R.id.fmAgenda)
    }

    private fun initListeners() {
        btnAddTask.setOnClickListener {
            showAddTask()
        }
        cvEscuela.setOnClickListener {
            toggleCategoryFilter(0)
            filterTasks()
        }
        cvTrabajo.setOnClickListener {
            toggleCategoryFilter(1)
            filterTasks()
        }
        cvPersonal.setOnClickListener {
            toggleCategoryFilter(2)
            filterTasks()
        }
        fmAgenda.setOnClickListener {
            val intent = Intent(this, AgendaActivity::class.java)
            startActivity(intent)
        }
        fmCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initUI() {
        tasksAdapter = TasksAdapter(tasks) { position -> onItemSelected(position) }
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = tasksAdapter

        tvSeccionName.text = "Lista de tareas"
        tvFecha.text = getCurrentDate()
        filterTasks()
    }


    private fun filterTasks() {
        val mapCategories = categoriasSeleccionadas.mapIndexed { index, isSelected ->
            if (isSelected) {
                when (index) {
                    0 -> "Escuela"
                    1 -> "Laboral"
                    2 -> "Personal"
                    else -> null
                }
            } else {
                null
            }
        }.filterNotNull()

        val tasksFiltered = if (mapCategories.isEmpty()) {
            tasks
        } else {

            tasks.filter { task -> task.category in mapCategories }
        }

        tasksAdapter.tasks = tasksFiltered
        tasksAdapter.notifyDataSetChanged()
    }


    private fun toggleCategoryFilter(index: Int) {
        categoriasSeleccionadas[index] = !categoriasSeleccionadas[index]

        val colorRes = if (categoriasSeleccionadas[index]) {
            R.color.disabled_category
        } else {
            R.color.background_categories
        }
        val color = ContextCompat.getColor(this, colorRes)

        when (index) {
            0 -> cvEscuela.setCardBackgroundColor(color)
            1 -> cvTrabajo.setCardBackgroundColor(color)
            2 -> cvPersonal.setCardBackgroundColor(color)
        }
    }

    private fun showAddTask() {
        //TODO: Hacer que los campos rellenados se guarden en la base de datos

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_new_task)
        val btnGuardar: Button = dialog.findViewById(R.id.btnGuardar)
        val etName: EditText = dialog.findViewById(R.id.etName)
        val etDescription: EditText = dialog.findViewById(R.id.etDescription)
        val spinnerCategorias: Spinner = dialog.findViewById(R.id.spinnerCategorias)
        val spinnerPrioridad: Spinner = dialog.findViewById(R.id.spinnerPrioridad)
        val etFecha: EditText = dialog.findViewById(R.id.etFecha)
        val etHora: EditText = dialog.findViewById(R.id.etHora)

        val opciones = arrayOf("Escuela", "Personal", "Laboral")
        val opcionesPrioridad = arrayOf("Urgente", "Media", "Baja")

        mostrarOpciones(spinnerCategorias, opciones)
        mostrarOpciones(spinnerPrioridad, opcionesPrioridad)

        btnGuardar.setOnClickListener {
            val name = etName.text.toString()
            val description = etDescription.text.toString()
            val categoria = spinnerCategorias.selectedItem.toString()
            val prioridad = spinnerPrioridad.selectedItem.toString()
            val fecha = etFecha.text.toString()
            val hora = etHora.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty() && fecha.isNotEmpty() && hora.isNotEmpty()) {
                val newTask = Task(name, categoria, fecha, hora, description, prioridad)
                tasks.add(newTask)
                // Después de añadir, volvemos a filtrar para que se muestre según los filtros actuales
                filterTasks()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun mostrarOpciones(spinner: Spinner, opciones: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun onItemSelected(position: Int) {
        val selectedTask = tasksAdapter.tasks[position]
        selectedTask.isSelected = !selectedTask.isSelected
        tasksAdapter.notifyItemChanged(position)
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}