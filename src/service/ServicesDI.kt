package com.wictorlyan.service

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

fun DI.MainBuilder.bindServices() {
    bind<ClinicService>() with singleton { ClinicService() }
    bind<ExaminationService>() with singleton { ExaminationService() }
}