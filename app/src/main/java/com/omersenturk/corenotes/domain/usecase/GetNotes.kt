package com.omersenturk.corenotes.domain.usecase

import com.omersenturk.corenotes.domain.model.Note
import com.omersenturk.corenotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotes(private val repository: NoteRepository) {

    operator fun invoke(): Flow<List<Note>>{
        return repository.getNotes()
    }
}