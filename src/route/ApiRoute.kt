package com.wictorlyan.route

import io.ktor.routing.Routing
import io.ktor.routing.route

fun Routing.apiRoute() {
    route("/api/v1") {
        clinics()
        examinations()
    }
}