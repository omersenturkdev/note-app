package com.omersenturk.corenotes.domain.usecase

import com.omersenturk.corenotes.domain.model.Note
import com.omersenturk.corenotes.domain.repository.NoteRepository

class GetNote(private val repository: NoteRepository) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}