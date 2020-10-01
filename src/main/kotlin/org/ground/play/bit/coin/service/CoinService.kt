package org.ground.play.bit.coin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.ground.play.bit.coin.dao.BitcoinDocumentPriceRepository
import org.ground.play.bit.coin.dto.Bitcoin
import org.ground.play.bit.coin.dto.BitcoinInformation
import org.ground.play.bit.coin.model.BitcoinDocumentPrice
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
    lateinit var bitcoinDocumentRepository: BitcoinDocumentPriceRepository

    val mapper = jacksonObjectMapper()

    val webClient = WebClient
            .builder()
            .baseUrl("https://cex.io")
            .build()

    suspend fun saveBitcoinPrice(): Bitcoin {
        val bitcoinResponse = getPojoFromRequest()
        println("Bitcoin response: $bitcoinResponse")

        try {
            val bitcoinPrice = BitcoinDocumentPrice(bitcoinResponse.lprice.toDouble(),
                    bitcoinResponse.curr1,
                    bitcoinResponse.curr2)
            val savedEntity = bitcoinDocumentRepository.save(bitcoinPrice)
            println("Bitcoin entity saved: $savedEntity")
        } catch (e: Exception) {
            println("Error while saving bitcoin ${e.message}")
        }

        return bitcoinResponse
    }

    suspend fun findPrice(time: String): Bitcoin {
        val dummyBitcoin = Bitcoin("0", "0", "0")
        println("Bitcoin: $dummyBitcoin")

        val localDate = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val findByCreatedDate = bitcoinDocumentRepository.findByCreatedDate(localDate)
        println("found: $findByCreatedDate")

        val res = if (findByCreatedDate.isEmpty()) {
            dummyBitcoin
        } else {
            val first = findByCreatedDate.get(0)
            Bitcoin(first.lprice.toString(), first.curr1, first.curr2)
        }

        return res
    }

    //TODO should return the DAO or a representation?
//    suspend fun currentPrice(): Bitcoin {
//        val bitcoinResponse = getPojoFromRequest()
//        println("Bitcoin request: $bitcoinResponse")
//
//        try {
//            val savedEntity = bitcoinRepository.save(bitcoinResponse)
//            println("Bitcoin entity: $savedEntity")
//        } catch (e: Exception) {
//            println("Error while saving bitcoint ${e.message}")
//        }
//
//        //TODO forma mas copada de hacer esto
//        return Bitcoin(bitcoinResponse.lprice, bitcoinResponse.curr1, bitcoinResponse.curr2)
//    }

//    suspend fun saveAndFind(time: String): Bitcoin {
//        val dummyBitcoin = BitcoinPrice("0", "0", "0",  LocalDateTime.now())
//        println("Bitcoin: $dummyBitcoin")
//
//        val bitcoinResponse = getPojoFromRequest()
//        println("Bitcoin to save: $bitcoinResponse")
//        bitcoinRepository.save(bitcoinResponse)
//
//        val findByCreatedDate = bitcoinRepository.findByCreatedDate(bitcoinResponse.createdDate)
//        println("found: $findByCreatedDate")
//
//        return Bitcoin(dummyBitcoin.lprice, dummyBitcoin.curr1, dummyBitcoin.curr2)
//    }

    suspend fun findAveragePrice(startTime: String, endTime: String): BitcoinInformation {
        val dummyBitcoin = BitcoinInformation(0.0, 0.0)
        println("Bitcoin: $dummyBitcoin")

        val localStartDate = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val localEndDate = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val findByCreatedDate = bitcoinDocumentRepository.findByCreatedDateBetween(localStartDate, localEndDate)
        println("found range template: $findByCreatedDate")

        val findRangeByQuery = bitcoinDocumentRepository.findMe(localStartDate, localEndDate)
        println("found range me: $findRangeByQuery")
        println("found range me: ${findRangeByQuery.size}")

//        val maxPrice = bitcoinRepository.findTopOrderByLpriceDesc()
//        println("found max: $maxPrice")

        val average = findRangeByQuery.map { it.lprice.toDouble() }.average()
        dummyBitcoin.average = average

        return dummyBitcoin
    }

    private suspend fun getPojoFromRequest(): Bitcoin {
        val bitcoinApiResponse = callApi()
        val bitcoinResponse: Bitcoin = mapper.readValue(bitcoinApiResponse)
        return bitcoinResponse
    }

    private suspend fun callApi(): String {
        val getMethod = webClient.method(HttpMethod.GET).uri("/api/last_price/BTC/USD")
        val response = getMethod.awaitExchange().awaitBody<String>()
        return response
    }
}