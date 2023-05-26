package ru.altmanea.webapp.rest

import io.ktor.server.routing.*
import config.Config
import data.Group
import data.Stream
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.altmanea.webapp.repo.groupsRepo
import ru.altmanea.webapp.repo.streamsRepo

fun Route.streamRoutes() {
    route(Config.streamsPath) {
        repoRoutes(streamsRepo)
        get("{id}/groups"){
            val streamId =
                call.parameters["id"] ?: return@get call.respondText(
                    "Missing or malformed studentId",
                    status = HttpStatusCode.BadRequest
                )
            val group = streamsRepo.read(streamId) ?: return@get call.respondText(
                "No element with id $streamId",
                status = HttpStatusCode.NotFound
            )
            val groups = groupsRepo.read().mapNotNull{
                if (group.elem.groupIds.contains(it.id))
                    it
                else
                    null
            }
            if (groups.isEmpty()){
                call.respondText ("", status = HttpStatusCode.NotFound)
            }
            else {
                call.respond(groups)
            }
        }
        put("{id}") {
//            val id =
//                call.parameters["id"] ?: return@put call.respondText(
//                    "Missing or malformed studentId",
//                    status = HttpStatusCode.BadRequest
//                )
//
//            val stream = streamsRepo.read(id) ?: return@put call.respondText(
//                "No student with id $id",
//                status = HttpStatusCode.BadRequest
//            )
//
//            val newGroup = call.receive<String>()
//
//            val groupId = groupsRepo
//                .apply { create(Group(newGroup)) }
//                .read()
//                .findLast { it.elem.name == newGroup }?.id ?: return@put call.respondText(
//                "Не удалось добавить или найти эту группу $newGroup",
//                status = HttpStatusCode.BadRequest
//            )
//
//            val newStream = stream.elem.addGroup(groupId)
//
//            streamsRepo.update(Item(newStream, stream.id, stream.version))
//
//            call.respondText(
//                "Element updates correctly",
//                status = HttpStatusCode.Created
//            )
        }
    }
}