package com.omersenturk.corenotes.presentation.home

import com.omersenturk.corenotes.domain.model.Note

sealed class HomeEvent {
    data class DeleteNote(val note: Note) : HomeEvent()
    object ClearError : HomeEvent()
}