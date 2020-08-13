package com.wictorlyan.service

import com.wictorlyan.data.*
import com.wictorlyan.exception.RecordNotFoundException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

class ClinicService {
    fun findAllClinics(): Iterable<Clinic> = transaction {
        ClinicEntity.all().sortedBy { it.id }.map(ClinicEntity::toClinic)
    }

    fun findClinicById(id: Int): Clinic? = transaction {
        ClinicEntity.findById(id)?.toClinic()
    }

    fun findClinicByName(name: String): Clinic? = transaction {
        ClinicEntity.find { Clinics.name eq name }.limit(1).firstOrNull()?.toClinic()
    }

    fun addClinic(clinic: Clinic) = transaction {
        ClinicEntity.new {
            name = clinic.name
            phone = clinic.phone
            address = clinic.address
        }.toClinic()
    }

    fun updateClinic(clinicId: Int, clinic: Clinic) = transaction {
        val entity = ClinicEntity[clinicId]
        entity.name = clinic.name
        entity.phone = clinic.phone
        entity.address = clinic.address
        entity.toClinic()
    }

    fun deleteClinic(id: Int) = transaction {
        ClinicEntity[id].delete()
    }

    fun linkClinicWithExamination(clinicId: Int, examinationId: Int, ce: ClinicExamination) = transaction {
        val clinic = ClinicEntity.findById(clinicId) ?: throw RecordNotFoundException("Clinic not found")
        val examination = ExaminationEntity.findById(examinationId) 
            ?: throw RecordNotFoundException("Examination not found")
        val ceEntity = ClinicExaminationEntity.find { 
            (ClinicExaminations.clinic eq clinicId) and (ClinicExaminations.examination eq examinationId) 
        }.firstOrNull()
        
        if (ceEntity == null) {
            ClinicExaminationEntity.new {
                this.clinic = clinic
                this.examination = examination
                this.price = ce.price
            }
        } else {
            ceEntity.price = ce.price
        }
    }
    
    fun unlinkClinicFromExamination(clinicId: Int, examinationId: Int) = transaction {
        val clinic = ClinicEntity.findById(clinicId) ?: throw RecordNotFoundException("Clinic not found")
        val examination = ExaminationEntity.findById(examinationId)
            ?: throw RecordNotFoundException("Examination not found")
        val ce = ClinicExaminationEntity.find {
            (ClinicExaminations.clinic eq clinicId) and (ClinicExaminations.examination eq examinationId)
        }.firstOrNull()
        
        ce?.delete() ?: throw RecordNotFoundException("Examination is not linked to the Clinic")
    }
}