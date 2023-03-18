package utils

import utils.ScannerInput.readNextInt

object Utilities {


    @JvmStatic
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }


    @JvmStatic
    fun validRange(numberToCheck: Int, min: Int, max: Int): Boolean {
        return numberToCheck in min..max
    }

    @JvmStatic
    fun readValidPriority(prompt: String?): Int {
        var input =  readNextInt(prompt)
        do {
            if (validRange(input, 1 ,5))
                return input
            else {
                print("Invalid priority $input.")
                input = readNextInt(prompt)
            }
        } while (true)
    }
}