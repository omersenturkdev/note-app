package com.omersenturk.corenotes.presentation.home

import com.omersenturk.corenotes.domain.model.Note

data class HomeState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error : String? = null
)
