package com.amirkenesbay.redismap

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import redis.clients.jedis.Jedis

@Configuration
class RedisConfiguration(
    @Value("\${redis.host}") private val host: String,
    @Value("\${redis.port}") private val port: Int
) {
    @Bean
    fun jedis(): Jedis {
        return Jedis(host, port)
    }

    @Bean
    fun redisMap(jedis: Jedis): RedisMap {
        return RedisMap(jedis)
    }
}