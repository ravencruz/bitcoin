package org.ground.play.bit.coin.controller


import org.ground.play.bit.coin.dto.Bitcoin
import org.ground.play.bit.coin.service.CoinService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class CoinController {

    @Autowired
    lateinit var service: CoinService

    @GetMapping("/coin/price/current")
    suspend fun currentPrice() : ResponseEntity<Bitcoin> {
        val bitCoinPrice = service.currentPrice()
        val response = ResponseEntity.ok(bitCoinPrice)
        return response
    }

    @GetMapping("/coin/price")
    suspend fun saveSearch(@RequestParam time: String) : Bitcoin {
        println("time $time")
        return service.saveAndFind(time)
    }

    @GetMapping("/coin/find")
    suspend fun findBitcoin(@RequestParam time: String) : ResponseEntity<Bitcoin> {
        println("time $time")
        val coin = service.findPrice(time)
        val res = ResponseEntity.ok(coin)
        return res
    }

    @GetMapping("/coin/find/average")
    suspend fun priceAverage(@RequestParam time1: String, @RequestParam time2: String) : Bitcoin {

        return service.findAveragePrice(time1, time2)
    }
}