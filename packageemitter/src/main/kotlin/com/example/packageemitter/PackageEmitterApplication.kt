package com.example.packageemitter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PackageEmitterApplication

fun main(args: Array<String>) {
	runApplication<PackageEmitterApplication>(*args)
}
