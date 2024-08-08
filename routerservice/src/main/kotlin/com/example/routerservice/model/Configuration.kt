package com.example.routerservice.model

import com.example.packageemitter.model.Size
import jakarta.persistence.*

@Entity
@Table(name = "configuration")
data class Configuration(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    val size: Size,
    @Column(nullable = false)
    var isEnabled: Boolean
)