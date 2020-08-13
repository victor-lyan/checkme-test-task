package com.wictorlyan.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Clinics : IntIdTable() {
    val name = varchar("name", 100).uniqueIndex()
    val phone = varchar("phone", 20)
    val address = varchar("address", 255)
}

class ClinicEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClinicEntity>(Clinics)

    var name by Clinics.name
    var phone by Clinics.phone
    var address by Clinics.address
    val examinations by ClinicExaminationEntity referrersOn ClinicExaminations.clinic

    override fun toString(): String {
        return "Clinic($name, $phone, $address)"
    }

    fun toClinic() = Clinic(id.value, name, phone, address, examinations.map { it.toClinicExamination() }.toList())
}

data class Clinic(
    val id: Int,
    val name: String,
    val phone: String,
    val address: String,
    val examinations: List<ClinicExamination>? = null
)