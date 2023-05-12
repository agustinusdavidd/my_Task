package com.example.my_task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimeTaskAdapter (private val taskList: List<TimeRowModel>) :
    RecyclerView.Adapter<TimeTaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val timeTv: TextView = itemView.findViewById(R.id.timeTaskTv)
        private val taskTv: TextView = itemView.findViewById(R.id.nameTaskTv)

        fun bind(taskItem : TimeRowModel){
            timeTv.text = taskItem.hour
            taskTv.text = taskItem.task
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_time_everyday, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}