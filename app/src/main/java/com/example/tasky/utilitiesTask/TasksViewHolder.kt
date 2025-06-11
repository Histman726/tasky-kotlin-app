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
            tvTask.setTextColor(ContextCompat.getColor(tvTask.context, R.color.disable_task))
        }
        else{
            tvTask.paintFlags = tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            tvTask.setTextColor(ContextCompat.getColor(tvTask.context, R.color.black))
        }

        tvTask.text = task.name
        tvDescription.text = task.description

        val color = when (task.priority){
            "Urgente" -> R.color.border_task_urgente
            "Media" -> R.color.border_task_media
            "Baja" -> R.color.border_task_baja
            else -> R.color.black
        }

        cvBorder.setCardBackgroundColor(ContextCompat.getColor(cvBorder.context, color))

        val image = when (task.category){
            "Escuela" -> R.drawable.ic_escuela
            "Personal" -> R.drawable.ic_personal
            "Laboral" -> R.drawable.work_case_filled_svgrepo_com
            else -> R.drawable.ic_image
        }

        ivCategory.setImageResource(image)
    }
}
