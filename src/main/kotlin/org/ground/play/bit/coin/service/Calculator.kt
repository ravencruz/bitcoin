package org.ground.play.bit.coin.service

import org.ground.play.bit.coin.dto.BitcoinInformation
import org.ground.play.bit.coin.model.BitcoinPriceDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class Calculator {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    fun porcentualDifference(
        rangeCoins: List<BitcoinPriceDocument>,
        allCoins: List<BitcoinPriceDocument>
    ): BitcoinInformation {

        logger.info("found range me: $rangeCoins")
        logger.info("found range me: ${rangeCoins.size}")
        logger.info("size coinds ${allCoins.size}")

        val res = BitcoinInformation(0.0, 0.0)

        val averagePrice = if (rangeCoins.isNotEmpty()) rangeCoins.map { it.lprice }.average() else 1.0
        val maxPrice = allCoins.maxBy { it.lprice }?.lprice ?: 1.0

        val diffPorcentual = Math.abs(maxPrice - averagePrice) / Math.abs(averagePrice) * 100

        res.average = averagePrice
        res.differenceAverageMax = diffPorcentual

        return res
    }
}
