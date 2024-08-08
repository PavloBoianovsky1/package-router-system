package com.example.routerservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class RouterServiceApplication

fun main(args: Array<String>) {
	runApplication<RouterServiceApplication>(*args)
}
