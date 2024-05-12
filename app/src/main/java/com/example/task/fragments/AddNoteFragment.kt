package com.example.task.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.task.MainActivity
import com.example.task.R
import com.example.task.databinding.FragmentAddNoteBinding
import com.example.task.model.Task
import com.example.task.viewmodel.TaskViewModel


class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {

    private var addTaskBinding: FragmentAddNoteBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var addTaskView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addTaskBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTaskView = view
    }

    private fun saveTask(view: View) {
        val taskTitle = binding.addNoteTitle.text.toString().trim()
        val taskDesc = binding.addNoteDesc.text.toString().trim()
        val taskPriority = binding.addNotePriority.text.toString().trim()

        if (taskTitle.isNotEmpty()) {
            val task = Task(0, taskTitle, taskDesc, taskPriority)
            taskViewModel.addTask(task)

            Toast.makeText(addTaskView.context, "Task Added", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(addTaskView.context, "Please enter task title", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveMenu -> {
                saveTask(addTaskView)
                true
            }

            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskBinding = null
    }
}