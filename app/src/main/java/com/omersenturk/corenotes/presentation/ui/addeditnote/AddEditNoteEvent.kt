package com.omersenturk.corenotes.presentation.ui.addeditnote

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()
    object ClearError : AddEditNoteEvent()
}