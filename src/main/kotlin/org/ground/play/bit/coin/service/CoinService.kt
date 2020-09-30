package org.ground.play.bit.coin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.ground.play.bit.coin.dao.BitcoinPriceRepository
import org.ground.play.bit.coin.dto.Bitcoin
import org.ground.play.bit.coin.model.BitcoinPrice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

@Service
class CoinService {

    @Autowired
    lateinit var bitcoinRepository: BitcoinPriceRepository

    val mapper = jacksonObjectMapper()

    val webClient = WebClient
            .builder()
            .baseUrl("https://cex.io")
            .build()

    //TODO should return the DAO or a representation?
    suspend fun execute(): Bitcoin {
        val bitcoinResponse = getPojoFromRequest()
        println("Bitcoin: $bitcoinResponse")

        bitcoinRepository.save(bitcoinResponse)
        return Bitcoin(bitcoinResponse.lprice, bitcoinResponse.curr1, bitcoinResponse.curr2)
    }

    private suspend fun getPojoFromRequest(): BitcoinPrice {
        val bitcoinApiResponse = callApi()
        val bitcoinResponse: BitcoinPrice = mapper.readValue(bitcoinApiResponse)
        return bitcoinResponse
    }

    private suspend fun callApi(): String {
        val getMethod = webClient.method(HttpMethod.GET).uri("/api/last_price/BTC/USD")
        val response = getMethod.awaitExchange().awaitBody<String>()
        return response
    }
}