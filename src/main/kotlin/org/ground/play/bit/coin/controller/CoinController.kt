package org.ground.play.bit.coin.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.ground.play.bit.coin.dto.Bitcoin
import org.ground.play.bit.coin.repository.BitcoinRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange


@RestController
class CoinController {

    @Autowired
    lateinit var repository: BitcoinRepository

//    @Scheduled(fixedRate = 7000)
    @GetMapping("/coin")
    suspend fun coin() : Bitcoin {
        val webClient = WebClient
                .builder()
                .baseUrl("https://cex.io")
                .build()

        val bitcoinApiResonse = suspendedExchange(webClient)

        val mapper = jacksonObjectMapper()
        val bitcoinSample: Bitcoin = mapper.readValue(bitcoinApiResonse)
        println("Bitcoin: $bitcoinSample")

        repository.save(bitcoinSample)

        return bitcoinSample
    }

    private suspend fun suspendedExchange(client: WebClient): String {
        val getMethod = client.method(HttpMethod.GET).uri("/api/last_price/BTC/USD")
        val response = getMethod.awaitExchange().awaitBody<String>()
        return response
    }

}