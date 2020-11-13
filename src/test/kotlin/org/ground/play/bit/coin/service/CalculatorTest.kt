package org.ground.play.bit.coin.service

import org.ground.play.bit.coin.model.BitcoinPriceDocument
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CalculatorTest {

    private val calculator = Calculator()
    var range = mutableListOf<BitcoinPriceDocument>()
    var all = mutableListOf<BitcoinPriceDocument>()

    @BeforeEach
    fun setUp() {
        range.add(BitcoinPriceDocument(20.0, "", ""))
        range.add(BitcoinPriceDocument(30.0, "", ""))

        all.add(BitcoinPriceDocument(10.0, "", ""))
        all.add(BitcoinPriceDocument(20.0, "", ""))
        all.add(BitcoinPriceDocument(30.0, "", ""))
        all.add(BitcoinPriceDocument(40.0, "", ""))
        all.add(BitcoinPriceDocument(50.0, "", ""))
        all.add(BitcoinPriceDocument(60.0, "", ""))
    }

    @Test
    fun porcentualDifference() {
        val coinInfo = calculator.porcentualDifference(range, all)
        Assertions.assertEquals(5.0, 5.0, 0.001)
        Assertions.assertEquals(25.0, coinInfo.average, 0.001)
        Assertions.assertEquals(140.0, coinInfo.differenceAverageMax, 0.001)
    }

    @Test
    fun porcentualDifferenceEmpty() {
        val coinInfo = calculator.porcentualDifference(mutableListOf<BitcoinPriceDocument>(), mutableListOf<BitcoinPriceDocument>())
        Assertions.assertEquals(5.0, 5.0, 0.001)
        Assertions.assertEquals(1.0, coinInfo.average, 0.001)
        Assertions.assertEquals(0.0, coinInfo.differenceAverageMax, 0.001)
    }
}
