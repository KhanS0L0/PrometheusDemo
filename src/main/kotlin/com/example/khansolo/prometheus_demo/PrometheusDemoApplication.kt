package com.example.khansolo.prometheus_demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PrometheusDemoApplication

fun main(args: Array<String>) {
	runApplication<PrometheusDemoApplication>(*args)
}
