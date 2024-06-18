package com.amirkenesbay.redismap

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class RedisMapRunner(private val redisMap: RedisMap) : CommandLineRunner {
    override fun run(vararg args: String?) {
        println("Adding key1 with value 1")
        redisMap["key1"] = 1
        println("Adding key2 with value 2")
        redisMap["key2"] = 2

        println("Value for key1: ${redisMap["key1"]}")
        println("Map size: ${redisMap.size}")

        println("Entries: ${redisMap.entries}")
        println("Keys: ${redisMap.keys}")
        println("Values: ${redisMap.values}")

        println("Contains key 'key1': ${redisMap.containsKey("key1")}")
        println("Contains value 2: ${redisMap.containsValue(2)}")

        println("Removing key1")
        redisMap.remove("key1")
        println("Value for key1 after removal: ${redisMap["key1"]}")
        println("Is map empty: ${redisMap.isEmpty()}")

        redisMap.clear()
        println("Map size after clearing: ${redisMap.size}")
    }
}