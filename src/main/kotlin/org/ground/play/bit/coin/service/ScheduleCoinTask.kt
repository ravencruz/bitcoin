package org.ground.play.bit.coin.service

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class ScheduleCoinTask {

    @Autowired
    lateinit var service: CoinService

    @Scheduled(fixedRate = 10_000)
    fun reportTime() {
        println("The time is now ${DateTimeFormatter.ISO_LOCAL_TIME.format(LocalDateTime.now())}")

        runBlocking {
            launch {
                service.saveBitcoinPrice()
            }
        }
    }
}