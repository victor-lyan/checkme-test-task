package com.wictorlyan.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Examinations : IntIdTable() {
    val name = varchar("name", 255)
    val averageDurationMinutes = integer("average_duration_minutes")
    val isDangerous = bool("is_dangerous").default(false)
}

class ExaminationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ExaminationEntity>(Examinations)
    
    var name by Examinations.name
    var averageDurationMinutes by Examinations.averageDurationMinutes
    var isDangerous by Examinations.isDangerous

    override fun toString(): String {
        return "Examination($name, $averageDurationMinutes, $isDangerous)"
    }
    
    fun toExamination() = Examination(id.value, name, averageDurationMinutes, isDangerous)
}

data class Examination(
    val id: Int,
    val name: String,
    val averageDurationMinutes: Int,
    val isDangerous: Boolean = false
)