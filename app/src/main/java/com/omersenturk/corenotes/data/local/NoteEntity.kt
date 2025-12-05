package com.omersenturk.corenotes.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omersenturk.corenotes.domain.model.Note

@Entity(tableName = "notes")
data class NoteEntity(
    val title: String,
    val content: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content
    )
}