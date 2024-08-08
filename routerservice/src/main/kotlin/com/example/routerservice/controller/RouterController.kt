package com.example.routerservice.controller

import com.example.routerservice.model.Configuration
import com.example.packageemitter.model.Package
import com.example.routerservice.repository.ConfigurationRepository
import com.example.routerservice.service.RouterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/router")
class RouterController(
    private val routerService: RouterService,
    private val configurationRepository: ConfigurationRepository
) {

    @PostMapping("/process")
    fun processPackage(@RequestBody pkg: Package): ResponseEntity<String> {
        return try {
            routerService.addPackageToQueue(pkg)
            ResponseEntity("Package added to the queue", HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Failed to add package to queue: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/configure")
    fun configure(@RequestBody configs: List<Configuration>): ResponseEntity<String> {
        return try {
            configurationRepository.saveAll(configs)
            ResponseEntity("Configurations updated successfully", HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Failed to update configurations: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/configure")
    fun updateOrAddConfigurations(@RequestBody configs: List<Configuration>): ResponseEntity<String> {
        val updatedConfigs = mutableListOf<Configuration>()
        val addedConfigs = mutableListOf<Configuration>()

        for (config in configs) {
            val existingConfig = configurationRepository.findBySize(config.size)
            if (existingConfig != null) {
                existingConfig.isEnabled = config.isEnabled
                updatedConfigs.add(configurationRepository.save(existingConfig))
                println("Configuration updated for size: ${config.size}")
            } else {
                addedConfigs.add(configurationRepository.save(config))
                println("Configuration added for size: ${config.size}")
            }
        }

        val responseMessage = buildString {
            append("Updated configurations: ${updatedConfigs.size}\n")
            append("Added configurations: ${addedConfigs.size}\n")
        }

        return if (updatedConfigs.isNotEmpty() || addedConfigs.isNotEmpty()) {
            ResponseEntity(responseMessage, HttpStatus.OK)
        } else {
            ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST)
        }
    }

}