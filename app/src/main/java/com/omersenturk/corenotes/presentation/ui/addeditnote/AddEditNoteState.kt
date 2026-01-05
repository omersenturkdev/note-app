package com.omersenturk.corenotes.presentation.ui.addeditnote

data class AddEditNoteState(
    val noteTitle: String = "",
    val noteContent: String = "",
    val noteId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val noteSaved: Boolean = false
)
