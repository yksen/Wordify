package com.wordify.model

class Word : ArrayList<WordItem>()

data class WordItem(
    val license: License,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>,
    val sourceUrls: List<String>,
    val word: String
)

data class License(
    val name: String,
    val url: String
)

data class Meaning(
    val antonyms: List<String>,
    val definitions: List<Definition>,
    val partOfSpeech: String,
    val synonyms: List<String>
)

data class Phonetic(
    val audio: String,
    val license: License,
    val sourceUrl: String,
    val text: String
)

data class Definition(
    val antonyms: List<Any>,
    val definition: String,
    val example: String,
    val synonyms: List<Any>
)