package com.omersenturk.corenotes.di

import android.content.Context
import androidx.room.Room
import com.omersenturk.corenotes.data.local.NoteDao
import com.omersenturk.corenotes.data.local.NoteDatabase
import com.omersenturk.corenotes.data.repository.NoteRepositoryImpl
import com.omersenturk.corenotes.domain.repository.NoteRepository
import com.omersenturk.corenotes.domain.usecase.DeleteNotes
import com.omersenturk.corenotes.domain.usecase.GetNote
import com.omersenturk.corenotes.domain.usecase.GetNotes
import com.omersenturk.corenotes.domain.usecase.InsertNotes
import com.omersenturk.corenotes.domain.usecase.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideNoteDao(db: NoteDatabase) = db.noteDao()

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            getNote = GetNote(repository),
            insertNotes = InsertNotes(repository),
            deleteNotes = DeleteNotes(repository)
        )
    }
}