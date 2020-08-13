package com.wictorlyan.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ClinicExaminations : IntIdTable() {
    val price = double("price")
    val clinic = reference("clinic", Clinics, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val examination = reference("examination", Examinations, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}

class ClinicExaminationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClinicExaminationEntity>(ClinicExaminations)
    
    var price by ClinicExaminations.price
    var clinic by ClinicEntity referencedOn ClinicExaminations.clinic
    var examination by ExaminationEntity referencedOn ClinicExaminations.examination
    
    fun toClinicExamination() = ClinicExamination(price, examination.toExamination())
}

data class ClinicExamination(
    val price: Double,
    val examination: Examination? = null
)