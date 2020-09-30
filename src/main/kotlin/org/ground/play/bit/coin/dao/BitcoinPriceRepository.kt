package org.ground.play.bit.coin.dao

import org.ground.play.bit.coin.model.BitcoinPrice
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BitcoinPriceRepository : MongoRepository<BitcoinPrice, String> {

    fun findByCreatedDate(@Param("createdDate") time: LocalDateTime): List<BitcoinPrice>


    fun findByCreatedDateTimeBetween(@Param("createdDate") start: LocalDateTime
                                    , @Param("createdDate") end: LocalDateTime): List<BitcoinPrice>

}