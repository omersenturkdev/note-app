package com.omersenturk.corenotes.domain.usecase

import com.omersenturk.corenotes.domain.model.Note
import com.omersenturk.corenotes.domain.repository.NoteRepository

class DeleteNote(private val repository: NoteRepository) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}