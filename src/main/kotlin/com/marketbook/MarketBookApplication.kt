package com.marketbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MarketBookApplication

fun main(args: Array<String>) {
	runApplication<MarketBookApplication>(*args)
}
