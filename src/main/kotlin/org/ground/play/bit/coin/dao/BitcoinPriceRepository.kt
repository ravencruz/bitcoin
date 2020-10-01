package org.ground.play.bit.coin.dao

import org.ground.play.bit.coin.model.BitcoinPriceDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BitcoinPriceRepository : MongoRepository<BitcoinPriceDocument, String> {

    fun findByCreatedDate(@Param("createdDate") time: LocalDateTime): List<BitcoinPriceDocument>


    fun findByCreatedDateBetween(@Param("createdDateStart") start: LocalDateTime
                                 , @Param("createdDateEnd") end: LocalDateTime): List<BitcoinPriceDocument>


    @Query("{'createdDate': {\$gte: ?0, \$lte:?1 }}")
    fun findBitcoinBetweenIncluded(@Param("createdDateStart") start: LocalDateTime
                                   , @Param("createdDateEnd") end: LocalDateTime): List<BitcoinPriceDocument>


}