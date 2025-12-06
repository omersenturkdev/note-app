package com.omersenturk.corenotes.presentation.home.addeditnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omersenturk.corenotes.domain.model.Note
import com.omersenturk.corenotes.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditNoteState())
    val state: StateFlow<AddEditNoteState> = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                loadNote(noteId)
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _state.value = state.value.copy(
                    noteTitle = event.value
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _state.value = state.value.copy(
                    noteContent = event.value
                )
            }
            is AddEditNoteEvent.SaveNote -> {
                saveNote()
            }
            is AddEditNoteEvent.ClearError -> {
                _state.value = state.value.copy(error = null)
            }
        }
    }

    private fun loadNote(id: Int) {
        viewModelScope.launch {
            val existingNote = noteUseCases.getNote(id)
            if (existingNote != null) {
                _state.value = state.value.copy(
                    noteId = existingNote.id,
                    noteTitle = existingNote.title,
                    noteContent = existingNote.content
                )
            } else {
                _state.value = state.value.copy(
                    error = "Not yüklenirken bir hata oluştu."
                )
            }
        }
    }

    private fun saveNote() {
        viewModelScope.launch {
            // Trimlenmiş versiyonları sadece doğrulama (validation) için kullanıyoruz.
            val trimmedTitle = state.value.noteTitle.trim()
            val trimmedContent = state.value.noteContent.trim()

            // Eğer başlık ve trimlenmiş içerik tamamen boşsa kaydetmeyi engelle.
            if (trimmedTitle.isBlank() && trimmedContent.isBlank()) {
                _state.value = state.value.copy(
                    error = "Başlık veya içerik boş olamaz."
                )
                return@launch
            }

            // İçeriği kaydederken: Eğer trimlenmiş içerik boşsa (kullanıcı hiç bir şey yazmamışsa),
            // varsayılan mesajı kullan. Aksi takdirde, kullanıcının girdiği ORİJİNAL (kesilmemiş)
            // içeriği kaydet (Böylece boşluk ve formatlama korunur).
            val contentToSave = if (trimmedContent.isBlank()) {
                "İçerik yok"
            } else {
                state.value.noteContent // Orijinal (Untrimmed) içeriği kaydet
            }

            try {
                val noteToSave = Note(
                    id = state.value.noteId,
                    title = trimmedTitle.ifBlank { "Başlıksız Not" },
                    content = contentToSave, // Artık orijinal içeriği kullanıyoruz
                )
                noteUseCases.insertNotes(noteToSave)

                _state.value = state.value.copy(
                    noteSaved = true,
                    error = null
                )

            } catch (e: Exception) {
                _state.value = state.value.copy(
                    error = "Not kaydedilirken beklenmedik bir hata oluştu: ${e.message}"
                )
            }
        }
    }
}