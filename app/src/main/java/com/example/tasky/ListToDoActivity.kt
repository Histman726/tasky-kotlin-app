package com.example.tasky

import android.os.Bundle
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
    }

    private fun initComponents() {
        val btnAddTask: FloatingActionButton = findViewById(R.id.btnAddTask)
        btnAddTask.setOnClickListener {
            showAddTask()
        }
        rvTasks = findViewById(R.id.rvTasks)
    }

    private fun showAddTask() {

    }

    private fun initUI(){
        tasksAdapter = TasksAdapter(tasks)
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = tasksAdapter
    }
}