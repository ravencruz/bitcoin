package org.ground.play.bit.coin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.ground.play.bit.coin.dao.BitcoinPriceRepository
import org.ground.play.bit.coin.dto.Bitcoin
import org.ground.play.bit.coin.dto.BitcoinInformation
import org.ground.play.bit.coin.model.BitcoinPriceDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
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

    suspend fun saveBitcoinPrice(): Bitcoin {
        val bitcoinResponse = getPojoFromRequest()
        println("Bitcoin response: $bitcoinResponse")

        try {
            val bitcoinPrice = BitcoinPriceDocument(bitcoinResponse.lprice.toDouble(),
                    bitcoinResponse.curr1,
                    bitcoinResponse.curr2)
            val savedEntity = bitcoinRepository.save(bitcoinPrice)
            println("Bitcoin entity saved: $savedEntity")
        } catch (e: Exception) {
            println("Error while saving bitcoin ${e.message}")
        }

        return bitcoinResponse
    }

    suspend fun findPrice(time: String): Bitcoin {
        val dummyBitcoin = Bitcoin("0", "0", "0", LocalDateTime.now())
        println("dummyBitcoin: $dummyBitcoin")

        val localDate = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val findByCreatedDate = bitcoinRepository.findByCreatedDate(localDate)
        println("found: $findByCreatedDate")

        val res = if (findByCreatedDate.isEmpty()) {
            dummyBitcoin
        } else {
            val first = findByCreatedDate.get(0)
            Bitcoin(first.lprice.toString(), first.curr1, first.curr2, first.createdDate)
        }

        return res
    }

    suspend fun findAll(): List<Bitcoin> {
        val dummyBitcoin = Bitcoin("0", "0", "0", LocalDateTime.now())
        println("dummyBitcoin: $dummyBitcoin")

        val allCoins = bitcoinRepository.findAll(Sort.by(Sort.Direction.DESC, "lprice"))
        val allCoinsDTO = allCoins.map { Bitcoin(it.lprice.toString(), it.curr1, it.curr2, it.createdDate) }

        return allCoinsDTO
    }

    suspend fun findAveragePrice(startTime: String, endTime: String): BitcoinInformation {
        val dummyBitcoin = BitcoinInformation(0.0, 0.0)
        val localStartDate = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val localEndDate = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val findByCreatedDate = bitcoinRepository.findByCreatedDateBetween(localStartDate, localEndDate)
        println("found range template: $findByCreatedDate")

        val bitcoinInRange = bitcoinRepository.findMe(localStartDate, localEndDate)
        println("found range me: $bitcoinInRange")
        println("found range me: ${bitcoinInRange.size}")

        //val sorting = Sort(Sort.Direction.DESC, "lprice")
//        bitcoinDocumentRepository.findAll(sorting)
        val allCoins = bitcoinRepository.findAll(Sort.by(Sort.Direction.DESC, "lprice"))
        println("size coinds ${allCoins.size}")
        println(" first coin ${allCoins[0]}")
        val maxPrice = allCoins[0].lprice

//        val maxPrice = bitcoinRepository.findTopOrderByLpriceDesc()
//        println("found max: $maxPrice")

        val averagePrice = bitcoinInRange.map { it.lprice }.average()
        val diferenciaPorcentual =  Math.abs( maxPrice - averagePrice ) / averagePrice * 100

        dummyBitcoin.average = averagePrice
        dummyBitcoin.differenceAverageMax = diferenciaPorcentual
        println("Bitcoin: $dummyBitcoin")

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