package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))
    private var emptyNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))


    @BeforeEach
    fun setup(){
        learnKotlin = Note("Learning Kotlin", 5, "College", "Chapter 1 assignment", "1/1/2023",false, false, false)
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", "holiday, food, sun", "10/6/2022", false, false, false)
        codeApp = Note("Code App", 4, "Work", "Add, functions, classes", "6/2/2023", true, true, true)
        testApp = Note("Test App", 4, "Work", "test functionality and view UI", "12/3/2023",false, false, false)
        swim = Note("Swim - Pool", 3, "Hobby", "Practice front-stroke", "1/5/2023",true, true, true)

        //adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown(){
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", "Study Chapter 2", "2/2/2023", false, false, false)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", "Study Chapter 2", "2/2/2023",false, false, false)
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class ListNotes {

        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }

        @Test
        fun `listActiveNotes returns no active notes stored when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
            assertTrue(
                emptyNotes!!.listActiveNotes().lowercase().contains("no active notes")
            )
        }

        @Test
        fun `listActiveNotes returns active notes when ArrayList has active notes stored`() {
            assertEquals(3, populatedNotes!!.numberOfActiveNotes())
            val activeNotesString = populatedNotes!!.listActiveNotes().lowercase()
            assertTrue(activeNotesString.contains("learning kotlin"))
            assertFalse(activeNotesString.contains("code app"))
            assertTrue(activeNotesString.contains("summer holiday"))
            assertTrue(activeNotesString.contains("test app"))
            assertFalse(activeNotesString.contains("swim"))
        }

        @Test
        fun `listArchivedNotes returns no archived notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
            assertTrue(
                emptyNotes!!.listArchivedNotes().lowercase().contains("no archived notes")
            )
        }

        @Test
        fun `listArchivedNotes returns archived notes when ArrayList has archived notes stored`() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            val archivedNotesString = populatedNotes!!.listArchivedNotes().lowercase(Locale.getDefault())
            assertFalse(archivedNotesString.contains("learning kotlin"))
            assertTrue(archivedNotesString.contains("code app"))
            assertFalse(archivedNotesString.contains("summer holiday"))
            assertFalse(archivedNotesString.contains("test app"))
            assertTrue(archivedNotesString.contains("swim"))
        }

        @Test
        fun `listFavouritedNotes returns no favourited notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfFavouritedNotes())
            assertTrue(
                emptyNotes!!.listFavouritedNotes().lowercase().contains("no favourited notes")
            )
        }

        @Test
        fun `listFavouritedNotes returns favourited notes when ArrayList has favourited notes stored`() {
            assertEquals(2, populatedNotes!!.numberOfFavouritedNotes())
            val favouritedNotesString = populatedNotes!!.listFavouritedNotes().lowercase(Locale.getDefault())
            assertFalse(favouritedNotesString.contains("learning kotlin"))
            assertTrue(favouritedNotesString.contains("code app"))
            assertFalse(favouritedNotesString.contains("summer holiday"))
            assertFalse(favouritedNotesString.contains("test app"))
            assertTrue(favouritedNotesString.contains("swim"))
        }

        @Test
        fun `listFinishedNotes returns no finished notes stored when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfFinishedNotes())
            assertTrue(
                emptyNotes!!.listFinishedNotes().lowercase().contains("no finished notes")
            )
        }

        @Test
        fun `listFinishedNotes returns finished notes when ArrayList has finished notes stored`() {
            assertEquals(2, populatedNotes!!.numberOfFinishedNotes())
            val finishedNotesString = populatedNotes!!.listFinishedNotes().lowercase(Locale.getDefault())
            assertFalse(finishedNotesString.contains("learning kotlin"))
            assertTrue(finishedNotesString.contains("code app"))
            assertFalse(finishedNotesString.contains("summer holiday"))
            assertFalse(finishedNotesString.contains("test app"))
            assertTrue(finishedNotesString.contains("swim"))
        }

        @Test
        fun `listNotesBySelectedPriority returns No Notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listNotesBySelectedPriority(1).lowercase().contains("no notes")
            )
        }

        @Test
        fun `listNotesBySelectedPriority returns no notes when no notes of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority2String = populatedNotes!!.listNotesBySelectedPriority(2).lowercase()
            assertTrue(priority2String.contains("no notes"))
            assertTrue(priority2String.contains("2"))
        }

        @Test
        fun `listNotesBySelectedPriority returns all notes that match that priority when notes of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority1String = populatedNotes!!.listNotesBySelectedPriority(1).lowercase()
            assertTrue(priority1String.contains("1 note"))
            assertTrue(priority1String.contains("priority 1"))
            assertTrue(priority1String.contains("summer holiday"))
            assertFalse(priority1String.contains("swim"))
            assertFalse(priority1String.contains("learning kotlin"))
            assertFalse(priority1String.contains("code app"))
            assertFalse(priority1String.contains("test app"))


            val priority4String = populatedNotes!!.listNotesBySelectedPriority(4).lowercase(Locale.getDefault())
            assertTrue(priority4String.contains("2 note"))
            assertTrue(priority4String.contains("priority 4"))
            assertFalse(priority4String.contains("swim"))
            assertTrue(priority4String.contains("code app"))
            assertTrue(priority4String.contains("test app"))
            assertFalse(priority4String.contains("learning kotlin"))
            assertFalse(priority4String.contains("summer holiday"))
        }
    }

    @Nested
    inner class DeleteNotes {

        @Test
        fun `deleting a Note that does not exist, returns null`() {
            assertNull(emptyNotes!!.deleteNote(0))
            assertNull(populatedNotes!!.deleteNote(-1))
            assertNull(populatedNotes!!.deleteNote(5))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(swim, populatedNotes!!.deleteNote(4))
            assertEquals(4, populatedNotes!!.numberOfNotes())
            assertEquals(learnKotlin, populatedNotes!!.deleteNote(0))
            assertEquals(3, populatedNotes!!.numberOfNotes())
        }
    }

    @Nested
    inner class UpdateNotes {
        @Test
        fun `updating a note that does not exist returns false`(){
            assertFalse(populatedNotes!!.updateNote(6, Note("Updating Note", 2, "Work", "test Update", "1/1/2023",false, false, false)))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updating Note", 2, "Work", "test Update", "1/1/2023", false, true, true)))
            assertFalse(emptyNotes!!.updateNote(0, Note("Updating Note", 2, "Work", "test Update", "1/1/2023", false, false, false)))
        }

        @Test
        fun `updating a note that exists returns true and updates`() {
            //check note 5 exists and check the contents
            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)

            //update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedNotes!!.updateNote(4, Note("Updating Note", 2, "College", "test Update", "1/1/2023",false, false, false)))
            assertEquals("Updating Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            //Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }
    }

    @Test
    fun `saving and loading an empty collection in JSON doesn't crash app`() {
        // Saving an empty notes.json file.
        val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
        storingNotes.store()

        //Loading the empty notes.json file into a new object
        val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
        loadedNotes.load()

        //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
        assertEquals(0, storingNotes.numberOfNotes())
        assertEquals(0, loadedNotes.numberOfNotes())
        assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
    }

    @Test
    fun `saving and loading an loaded collection in JSON doesn't loose data`() {
        // Storing 3 notes to the notes.json file.
        val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
        storingNotes.add(testApp!!)
        storingNotes.add(swim!!)
        storingNotes.add(summerHoliday!!)
        storingNotes.store()

        //Loading notes.json into a different collection
        val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
        loadedNotes.load()

        //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
        assertEquals(3, storingNotes.numberOfNotes())
        assertEquals(3, loadedNotes.numberOfNotes())
        assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
        assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
        assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
    }

    @Nested
    inner class ArchiveNotes {
        @Test
        fun `archiving a note that does not exist returns false`(){
            assertFalse(populatedNotes!!.archiveNote(6))
            assertFalse(populatedNotes!!.archiveNote(-1))
            assertFalse(emptyNotes!!.archiveNote(0))
        }

        @Test
        fun `archiving an already archived note returns false`(){
            assertTrue(populatedNotes!!.findNote(2)!!.isNoteArchived)
            assertFalse(populatedNotes!!.archiveNote(2))
        }

        @Test
        fun `archiving an active note that exists returns true and archives`() {
            assertFalse(populatedNotes!!.findNote(1)!!.isNoteArchived)
            assertTrue(populatedNotes!!.archiveNote(1))
            assertTrue(populatedNotes!!.findNote(1)!!.isNoteArchived)
        }
    }

    @Nested
    inner class FavouritedNotes {
        @Test
        fun `favouriting a note that does not exist returns false`(){
            assertFalse(populatedNotes!!.favouriteNote(6))
            assertFalse(populatedNotes!!.favouriteNote(-1))
            assertFalse(emptyNotes!!.favouriteNote(0))
        }

        @Test
        fun `favouriting an already favourited note returns false`(){
            assertTrue(populatedNotes!!.findNote(2)!!.isNoteFavourited)
            assertFalse(populatedNotes!!.favouriteNote(2))
        }

        @Test
        fun `favouriting any note that exists returns true and favourites`() {
            assertFalse(populatedNotes!!.findNote(1)!!.isNoteFavourited)
            assertTrue(populatedNotes!!.favouriteNote(1))
            assertTrue(populatedNotes!!.findNote(1)!!.isNoteFavourited)
        }
    }

    @Nested
    inner class FinishedNotes {
        @Test
        fun `marking finished a note that does not exist returns false`(){
            assertFalse(populatedNotes!!.finishNote(6))
            assertFalse(populatedNotes!!.finishNote(-1))
            assertFalse(emptyNotes!!.finishNote(0))
        }

        @Test
        fun `marking as finished an already finished note returns false`(){
            assertTrue(populatedNotes!!.findNote(2)!!.isNoteFinished)
            assertFalse(populatedNotes!!.finishNote(2))
        }

        @Test
        fun `finishing an active note that exists returns true and finishes`() {
            assertFalse(populatedNotes!!.findNote(1)!!.isNoteFinished)
            assertTrue(populatedNotes!!.finishNote(1))
            assertTrue(populatedNotes!!.findNote(1)!!.isNoteFinished)
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(0, emptyNotes!!.numberOfNotes())
        }

        @Test
        fun numberOfArchivedNotesCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
        }

        @Test
        fun numberOfActiveNotesCalculatedCorrectly() {
            assertEquals(3, populatedNotes!!.numberOfActiveNotes())
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
        }

        @Test
        fun numberOfFavouritedNotesCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfFavouritedNotes())
            assertEquals(0, emptyNotes!!.numberOfFavouritedNotes())
        }

        @Test
        fun numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(1))
            assertEquals(0, populatedNotes!!.numberOfNotesByPriority(2))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(3))
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(4))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(5))
            assertEquals(0, emptyNotes!!.numberOfNotesByPriority(1))
        }

        @Test
        fun numberOfNotesByCategoryCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByCategory("College"))
            assertEquals(1, populatedNotes!!.numberOfNotesByCategory("Holiday"))
            assertEquals(2, populatedNotes!!.numberOfNotesByCategory("Work"))
            assertEquals(1, populatedNotes!!.numberOfNotesByCategory("Hobby"))
            assertEquals(0, emptyNotes!!.numberOfNotesByCategory("Work"))
        }

        @Test
        fun numberOfNotesByTitleCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByTitle("Learning Kotlin"))
            assertEquals(1, populatedNotes!!.numberOfNotesByTitle("Summer Holiday to France"))
            assertEquals(1, populatedNotes!!.numberOfNotesByTitle("Code App"))
            assertEquals(1, populatedNotes!!.numberOfNotesByTitle("Test App"))
            assertEquals(1, populatedNotes!!.numberOfNotesByTitle("Swim - Pool"))
            assertEquals(0, emptyNotes!!.numberOfNotesByTitle("Learning Kotlin"))
        }
    }

    @Nested
    inner class SearchMethods {

        @Test
        fun `search notes by title returns no notes when no notes with that title exist`() {
            //Searching a populated collection for a title that doesn't exist.
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search notes by title returns notes when notes with that title exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())

            //Searching a populated collection for a full title that exists (case matches exactly)
            var searchResults = populatedNotes!!.searchByTitle("Code App")
            assertTrue(searchResults.contains("Code App"))
            assertFalse(searchResults.contains("Test App"))

            //Searching a populated collection for a partial title that exists (case matches exactly)
            searchResults = populatedNotes!!.searchByTitle("App")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))

            //Searching a populated collection for a partial title that exists (case doesn't match)
            searchResults = populatedNotes!!.searchByTitle("aPp")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))
        }
    }



}
