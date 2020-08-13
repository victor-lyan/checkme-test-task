package com.wictorlyan.exception

import java.lang.RuntimeException

class RecordNotFoundException(message: String?) : RuntimeException(message) {
    constructor() : this(null)
}