package com.omersenturk.corenotes.domain.usecase

data class NoteUseCases(
    val getNotes: GetNotes,
    val getNote: GetNote,
    val insertNotes: InsertNotes,
    val deleteNotes: DeleteNote
)
