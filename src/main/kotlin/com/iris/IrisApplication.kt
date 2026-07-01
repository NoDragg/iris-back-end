package com.iris

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IrisApplication

fun main(args: Array<String>) {
    runApplication<IrisApplication>(*args)
}
