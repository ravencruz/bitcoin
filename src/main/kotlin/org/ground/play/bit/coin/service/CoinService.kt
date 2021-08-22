package org.ground.play.bit.coin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.ground.play.bit.coin.dao.BitcoinPriceRepository
import org.ground.play.bit.coin.dto.Bitcoin
import org.ground.play.bit.coin.dto.BitcoinInformation
import org.ground.play.bit.coin.model.BitcoinPriceDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class CoinService(
    val calculator: Calculator,
    val bitcoinRepository: BitcoinPriceRepository
) {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)
    val mapper = jacksonObjectMapper()

    val webClient = WebClient
        .builder()
        .baseUrl("https://cex.io")
        .build()

    suspend fun saveBitcoinPrice(): Bitcoin {
        val bitcoinResponse = getPojoFromRequest()
        logger.info("Bitcoin response: $bitcoinResponse")

        try {
            val bitcoinPrice = BitcoinPriceDocument(
                bitcoinResponse.lprice.toDouble(),
                bitcoinResponse.curr1,
                bitcoinResponse.curr2
            )
            val savedEntity = bitcoinRepository.save(bitcoinPrice)
            logger.info("Bitcoin entity saved: $savedEntity")
        } catch (e: Exception) {
            logger.error("Error while saving bitcoin ${e.message}")
        }

        return bitcoinResponse
    }

    suspend fun findPrice(time: String): Bitcoin {
        val localDate = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val findByCreatedDate = bitcoinRepository
            .findByCreatedDate(localDate)
            .also {
                logger.info("found: $it")
            }

        return findByCreatedDate.getOrNull(0)?.toBitcoin() ?: Bitcoin("0", "0", "0")
    }

    suspend fun findAll() =
        bitcoinRepository
            .findAll(Sort.by(Sort.Direction.DESC, "lprice"))
            .map { it.toBitcoin() }


    suspend fun findAveragePrice(startTime: String, endTime: String): BitcoinInformation {
        val localStartDate = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val localEndDate = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val bitcoinInRange = bitcoinRepository.findBitcoinBetweenIncluded(localStartDate, localEndDate)
        val allCoins = bitcoinRepository.findAll(Sort.by(Sort.Direction.DESC, "lprice"))
        val dataBitcoin = calculator.porcentualDifference(bitcoinInRange, allCoins)
        logger.info("Bitcoin Information: $dataBitcoin")

        return dataBitcoin
    }

    private suspend fun getPojoFromRequest(): Bitcoin {
        val bitcoinApiResponse = callApi()
        logger.info("api response: $bitcoinApiResponse")
        val bitcoinResponse: Bitcoin = mapper.readValue(bitcoinApiResponse)
        return bitcoinResponse
    }

    private suspend fun callApi(): String {
        val getMethod = webClient.method(HttpMethod.GET).uri("/api/last_price/BTC/USD")
        val response = getMethod.awaitExchange().awaitBody<String>()
        return response
    }

    fun BitcoinPriceDocument.toBitcoin() =
        Bitcoin(
            this.lprice.toString(),
            this.curr1,
            this.curr2,
            this.createdDate,
        )
}
