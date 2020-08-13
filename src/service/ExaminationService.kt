package com.wictorlyan.service

import com.wictorlyan.data.*
import org.jetbrains.exposed.sql.transactions.transaction

class ExaminationService {
    fun findAllExaminations(): Iterable<Examination> = transaction {
        ExaminationEntity.all().sortedBy {it.id}.map(ExaminationEntity::toExamination)
    }

    fun findExaminationById(id: Int): Examination? = transaction {
        ExaminationEntity.findById(id)?.toExamination()
    }

    fun findExaminationByName(name: String): Examination? = transaction {
        ExaminationEntity.find { Examinations.name eq name }.limit(1).firstOrNull()?.toExamination()
    }

    fun addExamination(examination: Examination) = transaction {
        ExaminationEntity.new {
            name = examination.name
            averageDurationMinutes = examination.averageDurationMinutes
            isDangerous = examination.isDangerous
        }.toExamination()
    }

    fun updateExamination(examinationId: Int, examination: Examination) = transaction {
        val entity = ExaminationEntity[examinationId]
        entity.name = examination.name
        entity.averageDurationMinutes = examination.averageDurationMinutes
        entity.isDangerous = examination.isDangerous
        entity.toExamination()
    }

    fun deleteExamination(id: Int) = transaction {
        ExaminationEntity[id].delete()
    }
}