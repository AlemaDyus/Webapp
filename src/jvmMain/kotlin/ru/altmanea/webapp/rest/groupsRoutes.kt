package ru.altmanea.webapp.rest

import io.ktor.server.routing.*
import config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import ru.altmanea.webapp.repo.groupsRepo
import ru.altmanea.webapp.repo.streamsRepo

fun Route.groupRoutes() {
    route(Config.groupsPath) {
        repoRoutes(groupsRepo)
        get("streams/{groupId}") {
            val groupId =
                call.parameters["groupId"] ?: return@get call.respondText(
                    "Missing or malformed studentId",
                    status = HttpStatusCode.BadRequest
                )
            val group = streamsRepo.read().mapNotNull {
                if (groupId in it.elem.groupIds){
                    it.elem
                }
                else {
                    null
                }
            }
            if (group.isEmpty()){
                call.respondText ("", status = HttpStatusCode.NotFound)
            }
            else {
                call.respond(group)
            }
        }
//        get("{id}/subGroup") {
//            val id =
//                call.parameters["id"] ?: return@get call.respondText(
//                    "Missing or malformed id",
//                    status = HttpStatusCode.BadRequest
//                )
//            val item =
//                groupsRepo.read(id) ?: return@get call.respondText(
//                    "No element with id $id",
//                    status = HttpStatusCode.NotFound
//                )
////            val itemJson = Json.encodeToString(itemSerializer, item)
////            call.respond(itemJson)
//        }
    }
}