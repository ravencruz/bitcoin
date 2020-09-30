package org.ground.play.bit.coin.dao

import org.ground.play.bit.coin.dto.Bitcoin
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface BitcoinRepository : MongoRepository<Bitcoin, String> {
}