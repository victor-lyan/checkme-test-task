package com.wictorlyan.route

import com.wictorlyan.data.Examination
import com.wictorlyan.exception.RecordNotFoundException
import com.wictorlyan.service.ExaminationService
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.kodein.di.instance
import org.kodein.di.ktor.di
import java.security.InvalidParameterException

fun Route.examinations() {
    val examinationService by di().instance<ExaminationService>()

    get("examinations") {
        call.respond(ResponseSuccess(examinationService.findAllExaminations()))
    }

    get("examination/{id}") {
        try {
            val examinationId = call.parameters["id"]?.toIntOrNull() ?: throw InvalidParameterException()
            val examination = examinationService.findExaminationById(examinationId) ?: throw RecordNotFoundException()
            call.respond(HttpStatusCode.OK, ResponseSuccess(examination))
        } catch (e: InvalidParameterException) {
            call.respond(HttpStatusCode.BadRequest, ResponseError("Please, provide a valid examination id"))
        } catch (e: RecordNotFoundException) {
            call.respond(HttpStatusCode.NotFound, ResponseError("Examination not found"))
        }
    }

    post("examination") {
        try {
            val request = call.receive<Examination>()
            val examination = examinationService.addExamination(request)
            call.respond(HttpStatusCode.Created, ResponseSuccess(examination))
        } catch (e: Exception) {
            application.log.error("Failed to create an examination", e)
            call.respond(HttpStatusCode.BadRequest, ResponseError("Problems creating an Examination"))
        }
    }

    put("examination/{id}") {
        try {
            val examinationId = call.parameters["id"]?.toIntOrNull() ?: throw InvalidParameterException()
            val request = call.receive<Examination>()
            val examination = examinationService.updateExamination(examinationId, request)
            call.respond(HttpStatusCode.OK, ResponseSuccess(examination))
        } catch (e: InvalidParameterException) {
            call.respond(HttpStatusCode.BadRequest, ResponseError("Please, provide a valid examination id"))
        } catch (e: EntityNotFoundException) {
            call.respond(HttpStatusCode.NotFound, ResponseError("Examination not found"))
        } catch (e: Exception) {
            application.log.error("Failed to update an examination", e)
            call.respond(HttpStatusCode.BadRequest, ResponseError("Problems updating an Examination"))
        }
    }

    delete("examination/{id}") {
        try {
            val examinationId = call.parameters["id"]?.toIntOrNull() ?: throw InvalidParameterException()
            examinationService.deleteExamination(examinationId)
            call.respond(HttpStatusCode.OK, ResponseSuccess(null))
        } catch (e: InvalidParameterException) {
            call.respond(HttpStatusCode.BadRequest, ResponseError("Please, provide a valid examination id"))
        } catch (e: EntityNotFoundException) {
            call.respond(HttpStatusCode.NotFound, ResponseError("Examination not found"))
        } catch (e: Exception) {
            application.log.error("Failed to delete an examination", e)
            call.respond(HttpStatusCode.BadRequest, ResponseError("Problems deleting an Examination"))
        }
    }
}