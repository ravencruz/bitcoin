package org.ground.play.bit.coin.repository

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class BitcoinPrice (
        var lprice: String,
        var curr1: String,
        var curr2: String,
        var createdDate: LocalDateTime = LocalDateTime.now())