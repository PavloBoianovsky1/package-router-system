package com.example.clientapp.controller

import com.example.packageemitter.model.Package
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/client")
class ClientAppController {

    @PostMapping("/google")
    fun receiveGooglePackage(@RequestBody pkg: Package) {
        println("Google received package: $pkg")
    }

    @PostMapping("/atlassian")
    fun receiveAtlassianPackage(@RequestBody pkg: Package) {
        println("Atlassian received package: $pkg")
    }
}