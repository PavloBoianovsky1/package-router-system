package com.example.packageemitter.service

import com.example.packageemitter.model.Package
import com.example.packageemitter.model.Priority
import com.example.packageemitter.model.Receiver
import com.example.packageemitter.model.Size
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import kotlin.concurrent.thread


@Service
class PackageEmitterService(private val restTemplate: RestTemplate) {

    private var isRunning = false

    fun startEmitting() {
        isRunning = true
        thread {
            while (isRunning) {
                val randomPackage = generateRandomPackage()
                try {
                    sendPackageToRouter(randomPackage)
                } catch (e: Exception) {
                    println("Failed to send package: ${e.message}")
                }
                Thread.sleep(100)
            }
        }
    }

    fun stopEmitting() {
        isRunning = false
    }

    fun generateRandomPackage(): Package {
        val receivers = Receiver.entries.toTypedArray()
        val sizes = Size.entries.toTypedArray()
        val priorities = Priority.entries.toTypedArray()

        return Package(
            id = System.currentTimeMillis(),
            receiver = receivers.random(),
            size = sizes.random(),
            priority = priorities.random(),
            delay = (0..5).random()
        )
    }

    fun sendPackageToRouter(pkg: Package) {
        val responseEntity = restTemplate.postForEntity(
            "http://localhost:8081/router/process",
            pkg,
            Void::class.java
        )
        if (responseEntity.statusCode != HttpStatus.OK) {
            throw RuntimeException("Failed to send package to Router Service: ${responseEntity.statusCode}")
        }
    }
}