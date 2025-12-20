package com.omersenturk.corenotes.presentation.home.addeditnote

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
import com.google.android.material.snackbar.Snackbar
import com.omersenturk.corenotes.databinding.FragmentAddNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditNoteFragment : Fragment() {

    private val viewModel: AddEditNoteViewModel by viewModels()
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.fabSaveNote.setOnClickListener {
            viewModel.onEvent(AddEditNoteEvent.EnteredTitle(binding.editTextNoteTitle.text?.toString().orEmpty()))
            viewModel.onEvent(AddEditNoteEvent.EnteredContent(binding.editTextNoteContent.text?.toString().orEmpty()))

            viewModel.onEvent(AddEditNoteEvent.SaveNote)
        }

        binding.editTextNoteTitle.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.onEvent(AddEditNoteEvent.EnteredTitle(binding.editTextNoteTitle.text?.toString().orEmpty()))
            }
        }

        binding.editTextNoteContent.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.onEvent(AddEditNoteEvent.EnteredContent(binding.editTextNoteContent.text?.toString().orEmpty()))
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->

                    if (binding.editTextNoteTitle.text?.toString().orEmpty() != state.noteTitle) {
                        binding.editTextNoteTitle.setText(state.noteTitle)
                    }

                    if (binding.editTextNoteContent.text?.toString().orEmpty() != state.noteContent) {
                        binding.editTextNoteContent.setText(state.noteContent)
                    }

                    state.error?.let { message ->
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                        viewModel.onEvent(AddEditNoteEvent.ClearError)
                    }

                    if (state.noteSaved) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}