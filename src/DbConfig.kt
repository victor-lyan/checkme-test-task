package com.wictorlyan

import com.wictorlyan.data.ClinicExaminations
import com.wictorlyan.data.Clinics
import com.wictorlyan.data.Examinations
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

const val HIKARI_CONFIG_KEY = "ktor.hikariconfig"

@KtorExperimentalAPI
fun Application.initDB() {
    val configPath = environment.config.property(HIKARI_CONFIG_KEY).getString()
    val dbConfig = HikariConfig(configPath)
    val dataSource = HikariDataSource(dbConfig)
    
    Database.connect(dataSource)
    createTables()
}

private fun createTables() = transaction {
    SchemaUtils.create(
        Clinics,
        Examinations,
        ClinicExaminations
    )
}