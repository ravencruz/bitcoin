package org.ground.play.bit.coin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.ground.play.bit.coin.dto.Greeting
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange


@RestController
class CoinController {

    @GetMapping("/coin")
    suspend fun coin() : Greeting {
        val webClient = WebClient
                .builder()
                .baseUrl("https://cex.io")
                .build()

        val bitcoinApiResonse = suspendedExchange(webClient)

        val mapper = ObjectMapper()
        mapper.readTree(bitcoinApiResonse)

        return Greeting(1, bitcoinApiResonse.toString())
    }

    private suspend fun suspendedExchange(client: WebClient): String {
        val getMethod = client
                .method(HttpMethod.GET)
                .uri("/api/last_price/BTC/USD")

        val response = getMethod.awaitExchange().awaitBody<String>()

        return response
    }

}