package org.ground.play.bit.coin.dto

import java.time.LocalDateTime

data class Bitcoin(
    var lprice: String,
    var curr1: String,
    var curr2: String,
    var time: LocalDateTime = LocalDateTime.now()
) {
    var timeString: String = ""

    init {
        timeString = time.toString()
    }
}
