package com.omersenturk.corenotes.domain.usecase

import com.omersenturk.corenotes.domain.model.Note
import com.omersenturk.corenotes.domain.repository.NoteRepository

class InsertNotes(private val repository: NoteRepository) {

    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}