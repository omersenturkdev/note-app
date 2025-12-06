package com.omersenturk.corenotes.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.omersenturk.corenotes.databinding.FragmentHomeBinding
import com.omersenturk.corenotes.domain.model.Note
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            onNoteClick = ::handleNoteClick,
            onDeleteSwipe = { note ->
                viewModel.onEvent(HomeEvent.DeleteNote(note))
                Snackbar.make(binding.root, "Not silindi.", Snackbar.LENGTH_SHORT).show()
            }
        )
        binding.recycleViewNote.adapter = noteAdapter

        attachSwipeToDelete()
    }

    private fun setupListeners() {
        binding.ImageButtonHomeAdd.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddNoteFragment(noteId = -1)
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    noteAdapter.submitList(state.notes)

                    state.error?.let { errorMessage ->
                        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                        viewModel.onEvent(HomeEvent.ClearError)
                    }
                }
            }
        }
    }

    private fun handleNoteClick(note: Note) {
        val action = HomeFragmentDirections.actionHomeFragmentToAddNoteFragment(noteId = note.id ?: -1)
        findNavController().navigate(action)
    }

    private fun attachSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val noteToDelete = noteAdapter.currentList[position]

                noteAdapter.onDeleteSwipe(noteToDelete)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recycleViewNote)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}