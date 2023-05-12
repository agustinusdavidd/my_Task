package com.example.my_task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_task.databinding.FragmentCalendarBinding

class timeRowActivity : AppCompatActivity() {
    private lateinit var binding: FragmentCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentCalendar = FragmentCalendar()

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragmentCalendar)
            .commit()
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setUpAdapter()
//        setUpClickListener()
//        setUpCalendar()
        supportActionBar?.hide()
        val timeRv = findViewById<RecyclerView>(R.id.timeRv)
        val layoutManager = LinearLayoutManager(this)
        timeRv.layoutManager = layoutManager

        val taskList = mutableListOf<Pair<String, String>>()
        for (i in 0..23){
            val hour = "%02d:00".format(i)
            taskList.add(Pair(hour, ""))
        }
    }

    class FragmentCalendar : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View?{
            var binding = FragmentCalendarBinding.inflate(inflater, container, false)

            return binding.root
        }
    }
}