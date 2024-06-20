package com.amirkenesbay.redismap

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import redis.clients.jedis.Jedis
import java.util.AbstractMap
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class RedisMapTest {
    @Mock
    private lateinit var jedis: Jedis

    @InjectMocks
    private lateinit var redisMap: RedisMap

    @Test
    @DisplayName("Test putting a key-value pair and retrieving the value")
    fun `test put and get`() {
        // Given
        `when`(jedis.hget(anyString(), eq("key1"))).thenReturn("1")

        // When
        redisMap["key1"] = 1

        // Then
        assertEquals(1, redisMap["key1"])
    }

    @Test
    @DisplayName("Test removing a key-value pair")
    fun `test remove`() {
        // Given
        redisMap["key1"] = 1

        // When
        redisMap.remove("key1")

        // Then
        verify(jedis).hdel(anyString(), eq("key1"))
        `when`(jedis.hget(anyString(), eq("key1"))).thenReturn(null)
        assertNull(redisMap["key1"])
    }

    @Test
    @DisplayName("Test getting the size of the map")
    fun `test size`() {
        // Given
        `when`(jedis.hlen(anyString())).thenReturn(2L)

        // When
        redisMap["key1"] = 1
        redisMap["key2"] = 2

        // Then
        assertEquals(2, redisMap.size)
    }

    @Test
    @DisplayName("Test checking if the map is empty")
    fun `test isEmpty`() {
        // Given
        `when`(jedis.hlen(anyString())).thenReturn(0L)

        // When
        assertEquals(true, redisMap.isEmpty())

        redisMap["key1"] = 1
        `when`(jedis.hlen(anyString())).thenReturn(1L)

        // Then
        assertEquals(false, redisMap.isEmpty())
    }

    @Test
    @DisplayName("Test checking if the map contains a key")
    fun `test containsKey`() {
        // Given
        `when`(jedis.hexists(anyString(), eq("key1"))).thenReturn(true)
        `when`(jedis.hexists(anyString(), eq("key2"))).thenReturn(false)

        // When
        redisMap["key1"] = 1

        // Then
        assertEquals(true, redisMap.containsKey("key1"))
        assertEquals(false, redisMap.containsKey("key2"))
    }

    @Test
    @DisplayName("Test clearing all entries in the map")
    fun `test clear`() {
        // Given
        redisMap["key1"] = 1
        redisMap["key2"] = 2

        // When
        redisMap.clear()

        // Then
        verify(jedis).del(anyString())
        `when`(jedis.hlen(anyString())).thenReturn(0L)
        assertEquals(true, redisMap.isEmpty())
    }

    @Test
    @DisplayName("Test putting all key-value pairs from another map")
    fun `test putAll`() {
        // Given
        val mapToPut = mapOf("key1" to 1, "key2" to 2)

        // When
        redisMap.putAll(mapToPut)

        // Then
        verify(jedis).hset("redisMap", "key1", "1")
        verify(jedis).hset("redisMap", "key2", "2")
    }

    @Test
    @DisplayName("Test checking if the map contains a specific value")
    fun `test containsValue`() {
        // Given
        `when`(jedis.hvals("redisMap")).thenReturn(listOf("1", "2"))

        // When
        val containsValue1 = redisMap.containsValue(1)
        val containsValue3 = redisMap.containsValue(3)

        // Then
        assertTrue(containsValue1)
        assertFalse(containsValue3)
    }

    @Test
    @DisplayName("Test retrieving all values in the map")
    fun `test values`() {
        // Given
        `when`(jedis.hvals("redisMap")).thenReturn(listOf("1", "2", "3"))

        // When
        val values = redisMap.values

        // Then
        assertEquals(listOf(1, 2, 3), values.toList())
    }

    @Test
    @DisplayName("Test retrieving all entries in the map")
    fun `test entries`() {
        // Given
        `when`(jedis.hgetAll("redisMap")).thenReturn(mapOf("key1" to "1", "key2" to "2"))

        // When
        val entries = redisMap.entries

        // Then
        assertEquals(2, entries.size)
        assertTrue(entries.contains(AbstractMap.SimpleEntry("key1", 1)))
        assertTrue(entries.contains(AbstractMap.SimpleEntry("key2", 2)))
    }

    @Test
    @DisplayName("Test retrieving all keys in the map")
    fun `test keys`() {
        // Given
        val expectedKeys = setOf("key1", "key2", "key3")
        `when`(jedis.hkeys("redisMap")).thenReturn(expectedKeys)

        // When
        val keys = redisMap.keys

        // Then
        assertEquals(expectedKeys, keys)
    }
}