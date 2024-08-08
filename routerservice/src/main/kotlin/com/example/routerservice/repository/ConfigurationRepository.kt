package com.example.routerservice.repository

import com.example.packageemitter.model.Size
import com.example.routerservice.model.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConfigurationRepository : JpaRepository<Configuration, Long> {
    fun findBySize(size: Size): Configuration?
}