package org.ground.play.bit.coin.service

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class ScheduleCoinTask {

    @Autowired
    lateinit var service: CoinService

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @Scheduled(fixedRate = 30_000) // , initialDelay = 30_000
    fun reportTime() {
        logger.info("The time is now ${DateTimeFormatter.ISO_LOCAL_TIME.format(LocalDateTime.now())}")

        runBlocking {
            launch {
                try {
                    service.saveBitcoinPrice()
                } catch (e: Exception) {
                    logger.error("Error al guardar ${e.message}")
                }
            }
        }
    }
}
