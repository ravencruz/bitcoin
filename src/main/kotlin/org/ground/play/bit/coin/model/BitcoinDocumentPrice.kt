package org.ground.play.bit.coin.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class BitcoinDocumentPrice (
        var lprice: Double,
        var curr1: String,
        var curr2: String,
        var createdDate: LocalDateTime = LocalDateTime.now()
)