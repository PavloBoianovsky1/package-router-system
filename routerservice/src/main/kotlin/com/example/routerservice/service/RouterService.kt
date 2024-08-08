package com.example.routerservice.service

import com.example.packageemitter.model.Package
import com.example.packageemitter.model.Receiver
import com.example.routerservice.repository.ConfigurationRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


@Service
class RouterService(
    private val restTemplate: RestTemplate,
    private val configurationRepository: ConfigurationRepository
) {
    private val packageQueue = PriorityQueue<Package>(compareByDescending { it.priority.ordinal + it.size.ordinal })
    private val executor = Executors.newSingleThreadScheduledExecutor()

    fun processPackage(pkg: Package) {
        val config = configurationRepository.findBySize(pkg.size)
        if (config == null) {
            println("No configuration found for size: ${pkg.size}")
            return
        }
        if (config.isEnabled) {
            val delay = pkg.delay ?: 0
            val future: Future<*> = executor.schedule({
                try {
                    routeToClient(pkg)
                    println("Package with ID: ${pkg.id} processed successfully after $delay seconds delay.")
                } catch (e: Exception) {
                    println("Failed to process package with ID: ${pkg.id}: ${e.message}")
                }
            }, delay.toLong(), TimeUnit.SECONDS)
        } else {
            println("Package with ID: ${pkg.id} of size: ${pkg.size} is rejected due to configuration")
        }
    }

    private fun routeToClient(pkg: Package) {
        val url = when (pkg.receiver) {
            Receiver.ATLASSIAN -> "http://localhost:8082/client/atlassian"
            Receiver.GOOGLE -> "http://localhost:8082/client/google"
        }
        restTemplate.postForObject(url, pkg, Unit::class.java)
    }

    @Scheduled(fixedRate = 1000)
    fun processQueue() {
        while (packageQueue.isNotEmpty()) {
            println("Currently in que: ${packageQueue.size}")
            val pkg = packageQueue.poll()
            processPackage(pkg)
        }
    }

    fun addPackageToQueue(pkg: Package) {
        packageQueue.add(pkg)
    }
}