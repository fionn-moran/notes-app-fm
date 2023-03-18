package models

import java.text.DateFormat
import java.util.Date

data class Note(
    var noteTitle: String,
    var notePriority: Int,
    var noteCategory: String,
    var noteBody: String,
    var noteDate: String,
    var isNoteArchived: Boolean,
    var isNoteFavourited: Boolean
)
{
}