package org.ground.play.bit.coin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoinApplication

fun main(args: Array<String>) {
	runApplication<CoinApplication>(*args)
}
