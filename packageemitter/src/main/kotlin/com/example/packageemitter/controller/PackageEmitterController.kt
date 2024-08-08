package com.example.packageemitter.controller

import org.springframework.web.bind.annotation.*
import com.example.packageemitter.model.Package
import com.example.packageemitter.service.PackageEmitterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/emitter")
class PackageEmitterController(private val emitterService: PackageEmitterService) {

    @PostMapping("/start")
    fun start(): ResponseEntity<String> {
        try {
            emitterService.startEmitting()
            return ResponseEntity("Package emission started", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("Failed to start package emission: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/stop")
    fun stop(): ResponseEntity<String> {
        emitterService.stopEmitting()
        return ResponseEntity("Package emission stopped", HttpStatus.OK)
    }

    @PostMapping("/create")
    fun createPackage(@RequestBody pkg: Package): ResponseEntity<String> {
        return try {
            emitterService.sendPackageToRouter(pkg)
            ResponseEntity("Package sent successfully", HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Failed to send package: ${e.message}", HttpStatus.NOT_FOUND)
        }
    }
}