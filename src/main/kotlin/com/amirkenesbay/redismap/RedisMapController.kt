package com.amirkenesbay.redismap

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/redis-map")
class RedisMapController(private val redisMap: RedisMap) {
    @GetMapping("/size")
    fun getSize(): Int {
        return redisMap.size
    }

    @GetMapping("/entries")
    fun getEntries(): MutableSet<MutableMap.MutableEntry<String, Int>> {
        return redisMap.entries
    }

    @GetMapping("/keys")
    fun getKeys(): MutableSet<String> {
        return redisMap.keys
    }

    @GetMapping("/values")
    fun getValues(): MutableCollection<Int> {
        return redisMap.values
    }

    @DeleteMapping("/clear")
    fun clearMap() {
        redisMap.clear()
    }

    @GetMapping("/contains-key/{key}")
    fun containsKey(@PathVariable key: String): Boolean {
        return redisMap.containsKey(key)
    }

    @GetMapping("/contains-value/{value}")
    fun containsValue(@PathVariable value: Int): Boolean {
        return redisMap.containsValue(value)
    }

    @GetMapping("/get/{key}")
    fun getValue(@PathVariable key: String): Int? {
        return redisMap[key]
    }

    @GetMapping("/is-empty")
    fun isEmpty(): Boolean {
        return redisMap.isEmpty()
    }

    @PostMapping("/put")
    fun put(@RequestParam key: String, @RequestParam value: Int): Int? {
        return redisMap.put(key, value)
    }

    @PostMapping("/put-all")
    fun putAll(@RequestBody entries: Map<String, Int>) {
        redisMap.putAll(entries)
    }

    @DeleteMapping("/remove/{key}")
    fun remove(@PathVariable key: String): Int? {
        return redisMap.remove(key)
    }
}