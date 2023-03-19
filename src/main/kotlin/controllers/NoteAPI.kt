package controllers

import models.Note
import persistence.Serializer
import utils.Utilities
import utils.Utilities.isValidListIndex

// Serializer used for persistence - load/save notes
class NoteAPI(serializerType: Serializer) {
    private var notes = ArrayList<Note>()
    private var serializer: Serializer = serializerType

    // adds a new note to notes list
    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    // lists all notes
    fun listAllNotes(): String =
        if  (notes.isEmpty()) "No notes stored"
        else formatListString(notes)

    // lists all notes which have been archived
    fun listArchivedNotes(): String =
        if  (numberOfArchivedNotes() == 0) "No archived notes stored"
        else formatListString(notes.filter { note -> note.isNoteArchived})

    // lists all notes which have not been archived
    fun listActiveNotes(): String =
        if  (numberOfActiveNotes() == 0)  "No active notes stored"
        else formatListString(notes.filter { note -> !note.isNoteArchived})

    // lists all notes which have been marked as favourites
    fun listFavouritedNotes(): String =
        if  (numberOfFavouritedNotes() == 0)  "No favourited notes stored"
        else formatListString(notes.filter { note -> note.isNoteFavourited})

    // lists all notes which have been marked as finished
    fun listFinishedNotes(): String =
        if  (numberOfFinishedNotes() == 0)  "No notes marked as finished"
        else formatListString(notes.filter { note -> note.isNoteFinished})

    // lists all notes based on a specific priority number
    fun listNotesBySelectedPriority(priority: Int): String =
        if (notes.isEmpty()) "No notes stored"
        else {
            val listOfNotes = formatListString(notes.filter{ note -> note.notePriority == priority})
            if (listOfNotes.equals("")) "No notes with priority: $priority"
            else "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
        }

    // lists all notes based on a specific category
    fun listNotesBySelectedCategory(category: String): String =
        if (notes.isEmpty()) "No notes stored"
        else {
            val listOfNotes = formatListString(notes.filter{ note -> note.noteCategory == category})
            if (listOfNotes.equals("")) "No notes with category: $category"
            else "${numberOfNotesByCategory(category)} notes with category $category: $listOfNotes"
        }

    // Counts the total number of all notes
    fun numberOfNotes(): Int {
        return notes.size
    }

    // Retrieves a note bases on specific index
    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    // Counts the total number of all archived notes
    fun numberOfArchivedNotes(): Int = notes.count {
            note: Note -> note.isNoteArchived
    }

    // Counts the total number of all active notes
    fun numberOfActiveNotes(): Int = notes.count {
            note: Note -> !note.isNoteArchived
    }

    // Counts the total number of all favourited notes
    fun numberOfFavouritedNotes(): Int = notes.count {
            note: Note -> note.isNoteFavourited
    }

    // Counts the total number of all notes marked as finished
    fun numberOfFinishedNotes(): Int = notes.count {
            note: Note -> note.isNoteFinished
    }

    // Counts the total number of all notes with a specific priority number
    fun numberOfNotesByPriority(priority: Int): Int = notes.count {
        note: Note -> note.notePriority == priority
    }

    fun numberOfNotesByCategory(category: String): Int = notes.count {
            note: Note -> note.noteCategory == category
    }

    fun numberOfNotesByTitle(title: String): Int = notes.count {
            note: Note -> note.noteTitle == title
    }

    // deletes a specific note from notes list
    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }

    // updates a previously added note with additional/changed details
    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            foundNote.noteBody = note.noteBody
            foundNote.noteDate = note.noteDate
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    // checks if selected index is valid in the list
    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }

    // Read saved notes from serializer
    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    // write (save) notes to serializer
    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

    // function to archive a specific note
    fun archiveNote(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val noteToArchive = notes[indexToArchive]
            if (!noteToArchive.isNoteArchived) {
                noteToArchive.isNoteArchived = true
                return true
            }
        }
        return false
    }

    // function to favourite a specific note
    fun favouriteNote(indexToFavourite: Int): Boolean {
        if (isValidIndex(indexToFavourite)) {
            val noteToFavourite = notes[indexToFavourite]
            if (!noteToFavourite.isNoteFavourited) {
                noteToFavourite.isNoteFavourited = true
                return true
            }
        }
        return false
    }

    // function to mark a specific note as finished
    fun finishNote(indexToFinish: Int): Boolean {
        if (isValidIndex(indexToFinish)) {
            val noteToFinish = notes[indexToFinish]
            if (!noteToFinish.isNoteFinished) {
                noteToFinish.isNoteFinished = true
                return true
            }
        }
        return false
    }

    // searches for input text in note titles
    fun searchByTitle (searchString : String) =
        formatListString(
            notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })


    // helper function formats list of notes and returns as string
    private fun formatListString(notesToFormat : List<Note>) : String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }

}