package org.ground.play.bit.coin.controller


import org.ground.play.bit.coin.dto.Bitcoin
import org.ground.play.bit.coin.service.CoinService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class CoinController {

    @Autowired
    lateinit var service: CoinService

    @GetMapping("/coin")
    suspend fun coin() : Bitcoin {
        return service.execute()
    }
}