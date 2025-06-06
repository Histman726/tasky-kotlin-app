package com.example.tasky.utilitiesTask
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tasky.R

class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvTask: TextView = view.findViewById<TextView>(R.id.tvName)
    private val tvDescription: TextView = view.findViewById<TextView>(R.id.tvDescription)
    private val ivCategory: ImageView = view.findViewById<ImageView>(R.id.ivCategory)
    private val cvBorder: CardView = view.findViewById<CardView>(R.id.cvBorder)

    fun render(task: Task){
        if (task.isSelected){
            tvTask.paintFlags = tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        else{
            tvTask.paintFlags = tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        tvTask.text = task.name
        tvDescription.text = task.description
    }
}
