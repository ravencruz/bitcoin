package org.ground.play.bit.coin.controller


import org.ground.play.bit.coin.dto.Bitcoin
import org.ground.play.bit.coin.dto.BitcoinInformation
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

    @GetMapping("/coin/find")
    suspend fun findBitcoin(@RequestParam time: String) : ResponseEntity<Bitcoin> {
        println("time $time")
        val coin = service.findPrice(time)
        val res = ResponseEntity.ok(coin)
        return res
    }

    @GetMapping("/coin/find/average")
    suspend fun priceAverage(@RequestParam timeStart: String, @RequestParam timeEnd: String): ResponseEntity<BitcoinInformation> {
        println("timeStart $timeStart timeEnd $timeEnd")
        val info =  service.findAveragePrice(timeStart, timeEnd)
        val res = ResponseEntity.ok(info)
        return res
    }

    @GetMapping("/coin/find/all")
    suspend fun findAllBitcoins() : ResponseEntity<List<Bitcoin>> {
        println("findAllBitcoins")
        val coins = service.findAll()
        val res = ResponseEntity.ok(coins)
        return res
    }


}