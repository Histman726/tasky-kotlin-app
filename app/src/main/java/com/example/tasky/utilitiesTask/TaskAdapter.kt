package com.example.tasky.utilitiesTask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasky.R

class TasksAdapter(var tasks: List<Task>, private val onTaskSelected: (Int) -> Unit) : RecyclerView.Adapter<TasksViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task,parent, false)
        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TasksViewHolder,
        position: Int
    ) {
        holder.render(tasks[position])

        //TODO: Cambiar el clickListener
        holder.itemView.setOnClickListener { onTaskSelected(position) }
    }


    override fun getItemCount() = tasks.size
}