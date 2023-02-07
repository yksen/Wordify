package com.wordify.model

val letterFrequencies = mapOf(
    'e' to 12.70, 't' to 9.06, 'a' to 8.17, 'o' to 7.51, 'i' to 6.97,
    'n' to 6.75, 's' to 6.33, 'h' to 6.09, 'r' to 5.99, 'd' to 4.25,
    'l' to 4.03, 'c' to 2.78, 'u' to 2.76, 'm' to 2.41, 'w' to 2.36,
    'f' to 2.23, 'g' to 2.02, 'y' to 1.97, 'p' to 1.93, 'b' to 1.29,
    'v' to 0.98, 'k' to 0.77, 'j' to 0.15, 'x' to 0.15, 'q' to 0.10,
    'z' to 0.07
)

fun randomLetter(): Char {
    val totalFrequency = letterFrequencies.values.sum()
    val randomValue = Math.random() * totalFrequency
    var currentSum = 0.0
    for ((letter, frequency) in letterFrequencies) {
        currentSum += frequency
        if (currentSum >= randomValue) {
            return letter
        }
    }
    return ' '
}