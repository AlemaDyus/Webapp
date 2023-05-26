package ru.altmanea.webapp.repo

import data.Group
import data.Stream

val streamsRepo = ListRepo<Stream>()
val groupsRepo = ListRepo<Group>()

fun createTestData() {
    listOf(
        Group("20м", arrayOf("20м-1","20м-2")),
        Group("20и", arrayOf("20и-1","20и-2")),
        Group("20з", arrayOf("20з-1")),
        Group("21м", arrayOf("21м-1")),
        Group("21и", arrayOf("21и-1")),
        Group("21з", arrayOf("21з-1")),
        Group("29м", arrayOf("29м-1")),
        Group("29и", arrayOf("29и-1")),
        Group("29з", arrayOf("29з-1"),)
    ).map {
        groupsRepo.create(it)
    }

    val groups = groupsRepo.read()

    val groups20 = groups
        .filter { it.elem.name.startsWith("20") }
        .map { it.id }
    val groups21 = groups
        .filter { it.elem.name.startsWith("21") }
        .map { it.id }
    val groups29 = groups
        .filter { it.elem.name.startsWith("29") }
        .map { it.id }

    listOf(
        Stream("first", groups20),
        Stream("second", groups21),
        Stream("third", groups29)
    ).map {
        streamsRepo.create(it)
    }
}
