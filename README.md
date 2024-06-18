# RedisMap

RedisMap is a Redis-backed implementation of the MutableMap interface, allowing you to store key-value pairs in a Redis database and interact with them using standard Map operations.

## Usage

To use RedisMap, you can simply create an instance of it and use it like a regular MutableMap. For example:

```kotlin
val jedis = Jedis("localhost", 6379)
val redisMap = RedisMap(jedis)

redisMap["key1"] = 1
redisMap["key2"] = 2

println("Value for key1: ${redisMap["key1"]}")
println("Map size: ${redisMap.size}")

// Other operations ;)
```