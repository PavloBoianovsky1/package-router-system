package com.example.packageemitter.model

data class Package(
    val id: Long,
    val receiver: Receiver,
    val size: Size,
    val priority: Priority,
    val delay: Int? = 0
)

enum class Receiver {
    ATLASSIAN, GOOGLE
}

enum class Size {
    SMALL, MIDDLE, LARGE
}

enum class Priority {
    LOW, MID, HIGH
}