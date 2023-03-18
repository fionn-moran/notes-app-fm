package controllers

import models.Note
import persistence.Serializer

class NoteAPI(serializerType: Serializer) {
    private var notes = ArrayList<Note>()
    private var serializer: Serializer = serializerType

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String =
        if  (notes.isEmpty()) "No notes stored"
        else formatListString(notes)


    fun listArchivedNotes(): String =
        if  (numberOfArchivedNotes() == 0) "No archived notes stored"
        else formatListString(notes.filter { note -> note.isNoteArchived})

    fun listActiveNotes(): String =
        if  (numberOfActiveNotes() == 0)  "No active notes stored"
        else formatListString(notes.filter { note -> !note.isNoteArchived})

    fun listFavouritedNotes(): String =
        if  (numberOfFavouritedNotes() == 0)  "No favourited notes stored"
        else formatListString(notes.filter { note -> note.isNoteFavourited})

    fun listFinishedNotes(): String =
        if  (numberOfFinishedNotes() == 0)  "No notes marked as finished"
        else formatListString(notes.filter { note -> note.isNoteFinished})

    fun listNotesBySelectedPriority(priority: Int): String =
        if (notes.isEmpty()) "No notes stored"
        else {
            val listOfNotes = formatListString(notes.filter{ note -> note.notePriority == priority})
            if (listOfNotes.equals("")) "No notes with priority: $priority"
            else "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
        }

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun numberOfArchivedNotes(): Int = notes.count {
            note: Note -> note.isNoteArchived
    }


    fun numberOfActiveNotes(): Int = notes.count {
            note: Note -> !note.isNoteArchived
    }

    fun numberOfFavouritedNotes(): Int = notes.count {
            note: Note -> note.isNoteFavourited
    }

    fun numberOfFinishedNotes(): Int = notes.count {
            note: Note -> note.isNoteFinished
    }


    fun numberOfNotesByPriority(priority: Int): Int = notes.count {
        note: Note -> note.notePriority == priority
    }

    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }

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

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }

    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

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

    fun searchByTitle (searchString : String) =
        formatListString(
            notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })


    private fun formatListString(notesToFormat : List<Note>) : String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }

}