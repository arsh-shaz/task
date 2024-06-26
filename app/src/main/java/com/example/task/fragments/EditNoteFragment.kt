package com.example.task.fragments

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.example.task.MainActivity
import com.example.task.R
import com.example.task.databinding.FragmentEditNoteBinding
import com.example.task.model.Task
import com.example.task.viewmodel.TaskViewModel


class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {

    private var editTaskBinding: FragmentEditNoteBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var currentTask: Task

    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editTaskBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel

        currentTask = args.task!!

        binding.editNoteTitle.setText(currentTask.title)
        binding.editNoteDesc.setText(currentTask.content)
        binding.editNotePriority.setText(currentTask.priority)

        binding.editNoteFab.setOnClickListener {
            val taskTitle = binding.editNoteTitle.text.toString().trim()
            val taskDesc = binding.editNoteDesc.text.toString().trim()
            val taskPriority = binding.editNotePriority.text.toString().trim()

            if (taskTitle.isNotEmpty()) {
                val task = Task(currentTask.id, taskTitle, taskDesc, taskPriority)
                taskViewModel.updateTask(task)
                Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show()
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please enter task title", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun deleteTask() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Task")
            setMessage("Do you want to delete this task?")
            setPositiveButton("Delete") { _, _ ->
                taskViewModel.deleteTask(currentTask)
                Toast.makeText(context, "Task Deleted!", Toast.LENGTH_SHORT)
                    .show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteMenu -> {
                deleteTask()
                true
            }

            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTaskBinding = null
    }
}