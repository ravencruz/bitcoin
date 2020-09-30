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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
    suspend fun currentPrice(): Bitcoin {
        val bitcoinResponse = getPojoFromRequest()
        println("Bitcoin: $bitcoinResponse")

        bitcoinRepository.save(bitcoinResponse)
        //TODO forma mas copada de hacer esto
        return Bitcoin(bitcoinResponse.lprice, bitcoinResponse.curr1, bitcoinResponse.curr2)
    }

    suspend fun saveAndFind(time: String): Bitcoin {
        val dummyBitcoin = BitcoinPrice("0", "0", "0",  LocalDateTime.now())
        println("Bitcoin: $dummyBitcoin")

        val bitcoinResponse = getPojoFromRequest()
        println("Bitcoin to save: $bitcoinResponse")
        bitcoinRepository.save(bitcoinResponse)

        val findByCreatedDate = bitcoinRepository.findByCreatedDate(bitcoinResponse.createdDate)
        println("found: $findByCreatedDate")

        return Bitcoin(dummyBitcoin.lprice, dummyBitcoin.curr1, dummyBitcoin.curr2)
    }

    suspend fun findPrice(time: String): BitcoinPrice {
        val dummyBitcoin = BitcoinPrice("0", "0", "0",  LocalDateTime.now())
        println("Bitcoin: $dummyBitcoin")


        val localDate = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val findByCreatedDate = bitcoinRepository.findByCreatedDate(localDate)
        println("found: $findByCreatedDate")

        return findByCreatedDate.get(0)
    }

    suspend fun findAveragePrice(startTime: String, endTime: String): Bitcoin {
        val dummyBitcoin = BitcoinPrice("0", "0", "0",  LocalDateTime.now())
        println("Bitcoin: $dummyBitcoin")


        val localStartDate = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val localEndDate = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val findByCreatedDate = bitcoinRepository.findByCreatedDateTimeBetween(localStartDate, localEndDate)
        println("found range: $findByCreatedDate")


        return Bitcoin(dummyBitcoin.lprice, dummyBitcoin.curr1, dummyBitcoin.curr2)
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