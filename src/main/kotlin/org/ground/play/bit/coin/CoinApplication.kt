package org.ground.play.bit.coin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@EnableScheduling
@SpringBootApplication
class CoinApplication

fun main(args: Array<String>) {
    runApplication<CoinApplication>(*args)
}
