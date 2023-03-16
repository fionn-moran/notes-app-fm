import utils.ScannerInput
import java.lang.System.exit

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
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > ----------------------------------
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
            0 -> exitApp()
            else -> println("invalid option entered: ${option}")
        }
    } while (true)
}

fun addNote() {
    println("add note")
}

fun listNotes() {
    println("list notes")
}

fun updateNote() {
    println("update note")
}

fun deleteNote() {
    print("delete note")
}

fun exitApp() {
    println("exit app")
    exit(0)
}