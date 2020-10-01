package org.ground.play.bit.coin.service

import org.ground.play.bit.coin.dto.BitcoinInformation
import org.ground.play.bit.coin.model.BitcoinPriceDocument
import org.springframework.stereotype.Service

@Service
class Calculator {
    fun porcentualDifference(rangeCoins: List<BitcoinPriceDocument>,
                             allCoins: List<BitcoinPriceDocument>): BitcoinInformation {

        println("found range me: $rangeCoins")
        println("found range me: ${rangeCoins.size}")
        println("size coinds ${allCoins.size}")
        println(" first coin ${allCoins[0]}")

        val res = BitcoinInformation(0.0, 0.0)

        val averagePrice = rangeCoins.map { it.lprice }.average()
        val maxPrice = allCoins[0].lprice

        val diffPorcentual = Math.abs(maxPrice - averagePrice) / Math.abs(averagePrice) * 100

        res.average = averagePrice
        res.differenceAverageMax = diffPorcentual

        return res
    }
}