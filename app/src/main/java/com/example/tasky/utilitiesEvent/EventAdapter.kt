package com.example.tasky.utilitiesEvent

import kotlin.toString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasky.R

class EventAdapter(
    private val events: MutableList<Event>,
    private val onEventClick: (Event) -> Unit,
    private val onEventEdit: (Event, Int) -> Unit,
    private val onEventDelete: (Event, Int) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        val tvMonth: TextView = itemView.findViewById(R.id.tvMonth)
        val tvEventType: TextView = itemView.findViewById(R.id.tvEventType)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.tvDay.text = event.day.toString()
        holder.tvMonth.text = event.month
        holder.tvEventType.text = event.eventType
        holder.tvNote.text = if (event.note.isNotEmpty()) event.note else "Sin notas"

        holder.itemView.setOnClickListener {
            onEventClick(event)
        }

        holder.itemView.setOnLongClickListener {
            showEventMenu(holder.itemView, event, position)
            true
        }
    }

    private fun showEventMenu(view: View, event: Event, position: Int) {
        val popup = android.widget.PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.event_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    onEventEdit(event, position)
                    true
                }
                R.id.action_delete -> {
                    onEventDelete(event, position)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    fun updateEvent(position: Int, event: Event) {
        events[position] = event
        notifyItemChanged(position)
    }

    fun removeEvent(position: Int) {
        if (position >= 0 && position < events.size) {
            events.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, events.size)
        }
    }

    override fun getItemCount(): Int = events.size

    fun addEvent(event: Event) {
        events.add(event)
        notifyItemInserted(events.size - 1)
    }
}