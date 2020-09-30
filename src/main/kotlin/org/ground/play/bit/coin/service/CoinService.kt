package org.ground.play.bit.coin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.ground.play.bit.coin.dao.BitcoinRepository
import org.ground.play.bit.coin.dto.Bitcoin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

@Service
class CoinService {

    @Autowired
    lateinit var repository: BitcoinRepository

    val mapper = jacksonObjectMapper()

    val webClient = WebClient
            .builder()
            .baseUrl("https://cex.io")
            .build()

    suspend fun execute(): Bitcoin {
        val bitcoinResponse: Bitcoin = getPojoFromRequest()
        println("Bitcoin: $bitcoinResponse")

        repository.save(bitcoinResponse)
        return bitcoinResponse
    }

    private suspend fun getPojoFromRequest(): Bitcoin {
        val bitcoinApiResponse = externalRequest()
        val bitcoinResponse: Bitcoin = mapper.readValue(bitcoinApiResponse)
        return bitcoinResponse
    }

    private suspend fun externalRequest(): String {
        val getMethod = webClient.method(HttpMethod.GET).uri("/api/last_price/BTC/USD")
        val response = getMethod.awaitExchange().awaitBody<String>()
        return response
    }
}