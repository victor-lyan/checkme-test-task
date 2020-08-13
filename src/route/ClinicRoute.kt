package com.wictorlyan.route

import com.wictorlyan.data.Clinic
import com.wictorlyan.data.ClinicExamination
import com.wictorlyan.exception.RecordNotFoundException
import com.wictorlyan.service.ClinicService
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

fun Route.clinics() {
    val clinicService by di().instance<ClinicService>()
    
    get("clinics") {
        call.respond(ResponseSuccess(clinicService.findAllClinics()))
    }
    
    get("clinic/{id}") {
        try {
            val clinicId = call.parameters["id"]?.toIntOrNull() ?: throw InvalidParameterException()
            val clinic = clinicService.findClinicById(clinicId) ?: throw RecordNotFoundException()
            call.respond(HttpStatusCode.OK, ResponseSuccess(clinic))
        } catch (e: InvalidParameterException) {
            call.respond(HttpStatusCode.BadRequest, ResponseError("Please, provide a valid clinic id"))
        } catch (e: RecordNotFoundException) {
            call.respond(HttpStatusCode.NotFound, ResponseError("Clinic not found"))
        }
    }
    
    post("clinic") {
        try {
            val clinicRequest = call.receive<Clinic>()
            val clinic = clinicService.addClinic(clinicRequest)
            call.respond(HttpStatusCode.Created, ResponseSuccess(clinic))
        } catch (e: Exception) {
            application.log.error("Failed to create a clinic", e)
            call.respond(HttpStatusCode.BadRequest, ResponseError("Problems creating a Clinic"))
        }
    }
    
    put("clinic/{id}") {
        try {
            val clinicId = call.parameters["id"]?.toIntOrNull() ?: throw InvalidParameterException()
            val clinicRequest = call.receive<Clinic>()
            val clinic = clinicService.updateClinic(clinicId, clinicRequest)
            call.respond(HttpStatusCode.OK, ResponseSuccess(clinic))
        } catch (e: InvalidParameterException) {
            call.respond(HttpStatusCode.BadRequest, ResponseError("Please, provide a valid clinic id"))
        } catch (e: EntityNotFoundException) {
            call.respond(HttpStatusCode.NotFound, ResponseError("Clinic not found"))
        } catch (e: Exception) {
            application.log.error("Failed to update a clinic", e)
            call.respond(HttpStatusCode.BadRequest, ResponseError("Problems updating a Clinic"))
        }
    }
    
    delete("clinic/{id}") {
        try {
            val clinicId = call.parameters["id"]?.toIntOrNull() ?: throw InvalidParameterException()
            clinicService.deleteClinic(clinicId)
            call.respond(HttpStatusCode.OK, ResponseSuccess(null))
        } catch (e: InvalidParameterException) {
            call.respond(HttpStatusCode.BadRequest, ResponseError("Please, provide a valid clinic id"))
        } catch (e: EntityNotFoundException) {
            call.respond(HttpStatusCode.NotFound, ResponseError("Clinic not found"))
        } catch (e: Exception) {
            application.log.error("Failed to delete a clinic", e)
            call.respond(HttpStatusCode.BadRequest, ResponseError("Problems deleting Clinic"))
        }
    }
    
    post("clinic/{clinicId}/examination/{examinationId}") {
        try {
            val clinicId = call.parameters["clinicId"]?.toIntOrNull() 
                ?: throw InvalidParameterException("Please, provide a valid clinic id")
            val examinationId = call.parameters["examinationId"]?.toIntOrNull()
                ?: throw InvalidParameterException("Please, provide a valid examination id")
            val request = call.receive<ClinicExamination>()
            
            clinicService.linkClinicWithExamination(clinicId, examinationId, request)
            call.respond(HttpStatusCode.OK, ResponseSuccess(null))
        } catch (e: InvalidParameterException) {
            call.respond(HttpStatusCode.BadRequest, ResponseError(e.message ?: ""))
        } catch (e: RecordNotFoundException) {
            call.respond(HttpStatusCode.NotFound, ResponseError(e.message ?: ""))
        } catch (e: Exception) {
            application.log.error("Failed to link a clinic with an examination", e)
            call.respond(HttpStatusCode.BadRequest, ResponseError("Problems linking Clinic with Examination"))
        } 
    }
    
    delete("clinic/{clinicId}/examination/{examinationId}") {
        try {
            val clinicId = call.parameters["clinicId"]?.toIntOrNull()
                ?: throw InvalidParameterException("Please, provide a valid clinic id")
            val examinationId = call.parameters["examinationId"]?.toIntOrNull()
                ?: throw InvalidParameterException("Please, provide a valid examination id")

            clinicService.unlinkClinicFromExamination(clinicId, examinationId)
            call.respond(HttpStatusCode.OK, ResponseSuccess(null))
        } catch (e: InvalidParameterException) {
            call.respond(HttpStatusCode.BadRequest, ResponseError(e.message ?: ""))
        } catch (e: RecordNotFoundException) {
            call.respond(HttpStatusCode.NotFound, ResponseError(e.message ?: ""))
        } catch (e: Exception) {
            application.log.error("Failed to unlink a clinic from an examination", e)
            call.respond(HttpStatusCode.BadRequest, ResponseError("Problems unlinking Clinic with Examination"))
        }
    }
}