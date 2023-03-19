import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}

//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))





fun main(args: Array<String>) {
    runMenu()
}
fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List Notes                |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |   5) Archive Note              |
         > |   6) Search for Note           |
         > |   7) Favourite a Note          |
         > |   8) Finish Note               |
         > ----------------------------------
         > |   9) Count Notes by Priority   |
         > |  10) Count Notes by Category   |
         > |  11) Count Notes by title      |
         > |  12) Count All Notes           |
         > |  13) count Active Notes        |
         > |  14) Count Archived Notes      |
         > |  15) Count favourited Notes    |
         > |  16) Count Finished Notes      |
         > ----------------------------------
         > |  20) Save Notes                |
         > |  21) Load Notes                |
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            5 -> archiveNote()
            6 -> searchNotes()
            7 -> favouriteNote()
            8 -> finishNote()
            9 -> countByPriority()
            10 -> countByCategory()
            11 -> countByTitle()
            12 -> countAllNotes()
            13 -> countActiveNotes()
            14 -> countArchivedNotes()
            15 -> countFavouritedNotes()
            16 -> countFinishedNotes()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> println("invalid option entered: ${option}")
        }
    } while (true)
}

// Using user's input data, a note is added to NoteAPI
fun addNote(){
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val noteBody = readNextLine("Enter your note: ")
    val noteDate = readNextLine("Enter the note date: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, noteBody, noteDate, false, false, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

// list notes according to the user's input selection
fun listNotes() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > |   4) List FAVOURITED notes   |
                  > |   5) List FINISHED notes     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes();
            2 -> listActiveNotes();
            3 -> listArchivedNotes();
            4 -> listFavouritedNotes();
            5 -> listFinishedNotes();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}

// Update a current note
fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")
            val noteBody = readNextLine("Enter your note: ")
            val noteDate = readNextLine("Enter the note date: ")
            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, noteBody, noteDate, false, false, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

// delete a specific note by index
fun deleteNote(){
    //logger.info { "deleteNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

// list all active notes
fun listActiveNotes() {
    //logger.info { "listActiveNotes() function invoked" }
    println(noteAPI.listActiveNotes())
}

// list all archived notes
fun listArchivedNotes() {
    //logger.info { "listArchivedNotes() function invoked" }
    println(noteAPI.listArchivedNotes())
}

// list all favourited notes
fun listFavouritedNotes() {
    //logger.info { "listArchivedNotes() function invoked" }
    println(noteAPI.listFavouritedNotes())
}

// list all notes marked as finished
fun listFinishedNotes() {
    //logger.info { "listArchivedNotes() function invoked" }
    println(noteAPI.listFinishedNotes())
}

// calls store() function and saves notes file
fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

// calls load() function and reads from saved notes file
fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

// mark selected note as archived
fun archiveNote() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        //pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

// mark selected note as favourite
fun favouriteNote() {
    listAllNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to favourite if at least 1 note exists
        val indexToFavourite = readNextInt("Enter the index of the note to favourite: ")
        //pass the index of the note to NoteAPI for favouriting and check for success.
        if (noteAPI.favouriteNote(indexToFavourite)) {
            println("Your note has been favourited!")
        } else {
            println("Unfortunately your note has not been favourited!")
        }
    }
}

// mark selected note as finished
fun finishNote() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note to mark as finished if at least 1 active note exists
        val indexToFinish = readNextInt("Enter the index of the note to mark as finished: ")
        //pass the index of the note to NoteAPI and check for success.
        if (noteAPI.finishNote(indexToFinish)) {
            println("Your note has been marked as finished!")
        } else {
            println("Unfortunately your note has not been marked as finished!")
        }
    }
}

fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

// search for notes via titles
fun searchNotes() {
    val searchTitle = readNextLine("Enter the title to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}

// COUNTING IN MENU

fun countByCategory() {
    val category = readNextLine("Enter the category to count: ")
    println(noteAPI.numberOfNotesByCategory(category))
}

fun countByPriority() {
    val priority = readNextInt("Enter the priority number to count: ")
    println(noteAPI.numberOfNotesByPriority(priority))
}

fun countByTitle() {
    val title = readNextLine("Enter the title to count: ")
    println(noteAPI.numberOfNotesByTitle(title))
}

fun countAllNotes() {
    println(noteAPI.numberOfNotes())
}
fun countActiveNotes() {
    println(noteAPI.numberOfArchivedNotes())
}

fun countArchivedNotes() {
    println(noteAPI.numberOfArchivedNotes())
}

fun countFavouritedNotes() {
    println(noteAPI.numberOfFavouritedNotes())
}
fun countFinishedNotes() {
    println(noteAPI.numberOfFinishedNotes())
}
// Closes app
fun exitApp() {
    logger.info { "exitApp() function invoked" }
    exit(0)
}