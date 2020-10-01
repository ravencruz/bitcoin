package org.ground.play.bit.coin.dao

import org.ground.play.bit.coin.model.BitcoinDocumentPrice
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BitcoinDocumentPriceRepository : MongoRepository<BitcoinDocumentPrice, String> {

    fun findByCreatedDate(@Param("createdDate") time: LocalDateTime): List<BitcoinDocumentPrice>


    fun findByCreatedDateBetween(@Param("createdDateStart") start: LocalDateTime
                                 , @Param("createdDateEnd") end: LocalDateTime): List<BitcoinDocumentPrice>

    @Query("{'createdDate': {\$gte: ?0, \$lte:?1 }}")
    fun findMe(@Param("createdDateStart") start: LocalDateTime
               , @Param("createdDateEnd") end: LocalDateTime): List<BitcoinDocumentPrice>

    //OrderByLpriceDesc
//    fun findTopOrderByLpriceDesc(): BitcoinDocumentPrice
}