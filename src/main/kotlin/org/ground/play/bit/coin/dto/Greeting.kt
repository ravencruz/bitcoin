package org.ground.play.bit.coin.dto

/**
 * 1 only count on properties that are inside the construtor
 * 2 can override all those functions except for copy
 * 3 the primary constructor of a data class must have at least one parameter and lall of these must be val or var
 */
data class Greeting(val id: Long, val content: String)
