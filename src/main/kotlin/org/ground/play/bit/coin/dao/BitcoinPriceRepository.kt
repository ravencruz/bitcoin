package org.ground.play.bit.coin.dao

import org.ground.play.bit.coin.model.BitcoinPrice
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BitcoinPriceRepository : MongoRepository<BitcoinPrice, String> {
}