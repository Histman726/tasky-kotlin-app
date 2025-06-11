package com.example.tasky

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasky.utilitiesTask.Task
import com.example.tasky.utilitiesTask.TasksAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListToDoActivity : AppCompatActivity() {
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var rvTasks: RecyclerView
    private lateinit var btnAddTask: FloatingActionButton

    //TODO: Obtener las tareas de la base de datos
    //TODO: Hacer que se puedan crear nuevas tareas
    private val tasks = mutableListOf<Task>(
        Task("Hacer la tarea", "escolar", "Hoy", "10:00", "Descripción de la tarea", "Urgente"),
        Task("Hacer el excel", "laboral", "Hoy", "11:00", "Descripción de la tarea", "Urgente"),
        Task("Hacer la limpieza", "personal", "Hoy", "12:00", "Descripción de la tarea", "Urgente"),
    )

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
        initUI()
        initListeners()
    }

    private fun initComponents() {
        btnAddTask = findViewById(R.id.btnAddTask)
        btnAddTask.setOnClickListener {
            showAddTask()
        }
        rvTasks = findViewById(R.id.rvTasks)
        btnAddTask = findViewById(R.id.btnAddTask)
    }

    private fun initListeners() {
        btnAddTask.setOnClickListener {
            showAddTask()
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
        val opcionesPrioridad = arrayOf("Urgente", "Normal", "Baja")

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
                tasksAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun mostrarOpciones(spinner: Spinner, opciones: Array<String>){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun initUI(){
        tasksAdapter = TasksAdapter(tasks)
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = tasksAdapter
    }
}