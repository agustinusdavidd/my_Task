package com.example.my_task

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.style.EasyEditSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

class homeFragment : Fragment() {
    class Task(
        val todo: String,
        val deadline: String,
        val category: String,
        val contributor: String
    )

    private lateinit var todoEt: EditText
    private lateinit var categoryEt: AutoCompleteTextView
    private lateinit var contributorEt: EditText
    private lateinit var duetoEt: EditText
    private lateinit var timeEt: EditText
    private lateinit var addTaskBtn: Button

    private lateinit var doneTaskBtn: Button

    private lateinit var tvTaskName : TextView
    private lateinit var tvDetailTask : TextView
    private lateinit var ivTaskPict : ImageView

    private lateinit var taskList: MutableList<Task>
    private var currentTaskIndex = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this
        val name = "David"

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting: String = when (currentHour) {
            in 0..11 -> "Selamat Pagi, $name"
            in 12..15 -> "Selamat Siang, $name"
            in 16..18 -> "Selamat Sore, $name"
            else -> "Selamat Malam, $name"
        }

        val categories = arrayOf(
            "Pekerjaan", "Makan", "Belajar", "Jalan-jalan", "Belanja",
            "Olahraga", "Bank", "Istirahat", "Quality time", "Lain-lain"
        )

        val autoCompleteCategory =
            view.findViewById<AutoCompleteTextView>(R.id.autocompleteCategory)
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        autoCompleteCategory.setAdapter(adapter)

        todoEt = view.findViewById(R.id.etAddToDo)
        categoryEt = view.findViewById(R.id.autocompleteCategory)
        contributorEt = view.findViewById(R.id.etAddContributor)
        duetoEt = view.findViewById(R.id.etDueTo)
        timeEt = view.findViewById(R.id.etTime)

        doneTaskBtn = view.findViewById(R.id.doneTaskBtn)

        doneTaskBtn.setOnClickListener {
            var selectedIndex = currentTaskIndex
            taskList.removeAt(selectedIndex)
        }

        addTaskBtn = view.findViewById(R.id.addBtn)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        //Listener ketika add task ditekan
        addTaskBtn.setOnClickListener{
            val todo = todoEt.text.toString()
            val category = categoryEt.text.toString()
            val contributor = contributorEt.text.toString()
            val dueto = duetoEt.text.toString()
            val time = timeEt.text.toString()

            val task = Task(todo, "$dueto at $time", category, contributor)

            //Ambil daftar task yang ada dari shared preferences
            val taskListJson = sharedPreferences.getString("TASK_LIST", "[]")
            val taskList = Gson().fromJson<List<Task>>(taskListJson, object : TypeToken<List<Task>>() {}.type).toMutableList()

            //Tambah task baru
            taskList.add(task)

            //Simpan kedalam local storage sebagai string JSON
            val editor = sharedPreferences.edit()
            editor.putString("TASK_LIST", Gson().toJson(taskList))
            editor.apply()

            Toast.makeText(requireContext(), "Aktivitas Ditambahkan!", Toast.LENGTH_SHORT).show()

            //Membersihkan nilai input
            todoEt.setText("")
            categoryEt.setText("")
            contributorEt.setText("")
            duetoEt.setText("")
            timeEt.setText("")
            updateLayout(currentTaskIndex, view)
        }

        val nextTaskBtn = view.findViewById<ImageButton>(R.id.nextTask)
        nextTaskBtn.setOnClickListener {
            if (currentTaskIndex < taskList.size - 1) {
                currentTaskIndex ++
                updateLayout(currentTaskIndex, view)
            }
        }
        val prevTaskBtn = view.findViewById<ImageButton>(R.id.prevTask)
        prevTaskBtn.setOnClickListener {
            if(currentTaskIndex > 0) {
                currentTaskIndex --
                updateLayout(currentTaskIndex, view)
            }
        }

        doneTaskBtn.setOnClickListener {
            doneTaskBtn.isEnabled = false
            val selectedIndex = currentTaskIndex
            val selectedTask = taskList[selectedIndex]
            taskList.removeAt(selectedIndex)

            val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val completedTask = sharedPreferences.getStringSet("COMPLETE_TASK", mutableSetOf())!!
            completedTask.add(Gson().toJson(selectedTask))

            val editor = sharedPreferences.edit()
            editor.putStringSet("COMPLETE_TASK", completedTask)
            editor.putString("TASK_LIST", Gson().toJson(taskList))
            editor.apply()
            if (taskList.isEmpty()) {
                tvTaskName.text = "There is nothing to do"
                tvDetailTask.text = "Add something to do"
                ivTaskPict.setImageResource(R.drawable.empty)
                doneTaskBtn.isEnabled = false
                Toast.makeText(requireContext(), "Tugas ${selectedTask.todo} telah selesai", Toast.LENGTH_SHORT). show()
            } else if(currentTaskIndex >= taskList.size){
                currentTaskIndex = 0
                updateLayout(currentTaskIndex, view)
                doneTaskBtn.isEnabled = true
                Toast.makeText(requireContext(), "Tugas ${selectedTask.todo} telah selesai", Toast.LENGTH_SHORT). show()
            } else {
                updateLayout(currentTaskIndex, view)
                doneTaskBtn.isEnabled = true
                Toast.makeText(requireContext(), "Tugas ${selectedTask.todo} telah selesai", Toast.LENGTH_SHORT). show()
            }
        }
        return view
    }

    private fun updateLayout(index: Int, view: View){
        var sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        val taskListJson = sharedPreferences?.getString("TASK_LIST", "")
        taskList = if (taskListJson.isNullOrEmpty()) {
            mutableListOf<Task>()
        } else {
            Gson().fromJson<List<Task>>(taskListJson, object : TypeToken<List<Task>>() {}.type).toMutableList()
        }

        val task = taskList[index]

        tvTaskName = view.findViewById(R.id.tvTaskName)
        tvTaskName.text = task.todo

        tvDetailTask = view.findViewById(R.id.tvDetailTask)
        tvDetailTask.text = "Due to ${task.deadline}"

        ivTaskPict = view.findViewById(R.id.ivTaskPicture)
        setTaskPicture(task.category, ivTaskPict)
    }

    fun setTaskPicture(category: String, ivTaskPict: ImageView){
        when (category) {
            "Pekerjaan" -> ivTaskPict.setImageResource(R.drawable.work)
            "Makan" -> ivTaskPict.setImageResource(R.drawable.eat)
            "Belajar" -> ivTaskPict.setImageResource(R.drawable.belajar)
            "Jalan-jalan" -> ivTaskPict.setImageResource(R.drawable.jalan_jalan)
            "Belanja" -> ivTaskPict.setImageResource(R.drawable.shopping)
            "Olahraga" -> ivTaskPict.setImageResource(R.drawable.workout)
            "Bank" -> ivTaskPict.setImageResource(R.drawable.bank)
            "Istirahat" -> ivTaskPict.setImageResource(R.drawable.istirahat)
            "Quality time" -> ivTaskPict.setImageResource(R.drawable.quality_time)
            "Lain-lain" -> ivTaskPict.setImageResource(R.drawable.etc)
            else -> ivTaskPict.setImageResource(R.drawable.empty)
        }
    }
}
