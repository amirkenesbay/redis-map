package com.amirkenesbay.redismap

import org.springframework.stereotype.Component
import redis.clients.jedis.Jedis
import java.util.*

private const val REDIS_HASH_NAME = "redisMap"

/**
 * A Redis-backed implementation of the MutableMap interface.
 * This class uses a Redis cluster to store data, providing a way to interact
 * with Redis using standard Map operations.
 */
@Component
class RedisMap(private val jedis: Jedis) : MutableMap<String, Int> {

    /**
     * Returns the number of key-value mappings in this map.
     *
     * The size method would require the use of the Redis HLEN command to count
     * the number of fields in the hash. This can be a slow operation, especially
     * for large datasets, as it involves retrieving metadata from Redis, which
     * may cause performance degradation.
     */
    override val size: Int
        get() = jedis.hlen(REDIS_HASH_NAME).toInt()

    /**
     * Returns a set of all key-value mappings contained in this map.
     *
     * This method fetches all entries from the Redis hash and constructs a mutable set of
     * key-value pairs represented as `MutableMap.MutableEntry<String, Int>`.
     *
     * @return A mutable set of key-value mappings contained in this map.
     */
    override val entries: MutableSet<MutableMap.MutableEntry<String, Int>>
        get() {
            val entries = jedis.hgetAll(REDIS_HASH_NAME)
            val result = mutableSetOf<MutableMap.MutableEntry<String, Int>>()
            entries.forEach { (key, value) ->
                result.add(AbstractMap.SimpleEntry(key, value.toInt()))
            }
            return result
        }

    /**
     * Returns a set of the keys contained in this map.
     * This method fetches all keys from the Redis hash.
     *
     * @return a set of the keys contained in this map.
     */
    override val keys: MutableSet<String>
        get() = jedis.hkeys(REDIS_HASH_NAME)

    /**
     * Returns a collection of the values contained in this map.
     * This method fetches all values from the Redis hash and converts them to integers.
     *
     * @return a collection of the values contained in this map.
     */
    override val values: MutableCollection<Int>
        get() {
            val values = jedis.hvals(REDIS_HASH_NAME)
            return values.map { it.toInt() }.toMutableList()
        }

    /**
     * Removes all the mappings from this map.
     */
    override fun clear() {
        jedis.del(REDIS_HASH_NAME)
    }

    /**
     * Returns true if the map contains the specified key.
     *
     * @param key the key to check for presence in the map.
     * @return true if the map contains the key, false otherwise.
     */
    override fun containsKey(key: String): Boolean = jedis.hexists(REDIS_HASH_NAME, key)

    /**
     * Returns `true` if this map contains the specified value.
     *
     * The containsValue method would require fetching all values in the hash using the Redis HVALS command,
     * followed by a linear search through these values. This can be very inefficient and resource-intensive,
     * especially for large hashes, potentially leading to significant performance issues.
     *
     * @param value the value whose presence in this map is to be tested
     * @return `true` if this map contains the specified value
     */
    override fun containsValue(value: Int): Boolean {
        val values = jedis.hvals(REDIS_HASH_NAME)
        return values.contains(value.toString())
    }

    /**
     * Returns the value associated with the specified key, or null if the key is not present.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value associated with the key, or null if the key is not present.
     */
    override fun get(key: String): Int? = jedis.hget(REDIS_HASH_NAME, key)?.toInt()

    /**
     * Returns true if the map contains no key-value pairs.
     *
     * @return true if the map is empty, false otherwise.
     */
    override fun isEmpty(): Boolean = size == 0

    /**
     * Associates the specified value with the specified key in the map.
     * If the map previously contained a mapping for the key, the old value is replaced by the specified value.
     *
     * @param key the key with which the specified value is to be associated.
     * @param value the value to be associated with the specified key.
     * @return the previous value associated with the key, or null if there was no mapping for the key.
     */
    override fun put(key: String, value: Int): Int? {
        jedis.hset(REDIS_HASH_NAME, key, value.toString())
        return value
    }

    /**
     * Copies all the mappings from the specified map to this map.
     *
     * @param from the map from which to copy mappings.
     */
    override fun putAll(from: Map<out String, Int>) {
        from.forEach { (key, value) -> put(key, value) }
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key the key whose mapping is to be removed from the map.
     * @return the previous value associated with the key, or null if there was no mapping for the key.
     */
    override fun remove(key: String): Int? {
        val value = get(key)
        jedis.hdel(REDIS_HASH_NAME, key)
        return value
    }
}