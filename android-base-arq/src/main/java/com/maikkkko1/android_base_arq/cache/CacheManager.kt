package com.maikkkko1.android_base_arq.cache

import org.joda.time.LocalDateTime
import java.util.*

/**
 * Cache any type of data that can be used until the application dies.
 *
 */
class CacheManager {
    companion object {
        /**
         * Put a new item in the cache list.
         *
         * @param cacheItem
         */
        fun add(cacheItem: CacheItem) {
            with(CacheDataHolder) {
                val findCacheItem = cache.find { it.key == cacheItem.key }

                if (findCacheItem != null) {
                    cache.remove(findCacheItem)
                }

                cacheItem.let {
                    if (it.durationInMinutes != null && it.durationInMinutes > 0) {
                        it.expiresAt = Calendar.getInstance().apply {
                            add(Calendar.MINUTE, it.durationInMinutes)
                        }.time
                    }
                }

                cache.add(cacheItem)
            }
        }

        /**
         * Returns a cached item with the Any type (you must cast it as you wish).
         *
         * @param key
         * @return
         */
        fun fetch(key: String): CacheItem? {
            val item = CacheDataHolder.cache.find { it.key == key }

            if (item != null) {
                val isExpired = if (item.expiresAt != null) {
                    LocalDateTime.now().isAfter(LocalDateTime(item.expiresAt))
                } else false

                if (!isExpired) return item
                else remove(key = key)
            }

            return null
        }

        /**
         * Removes the item from the cache list.
         *
         * @param key
         */
        fun remove(key: String) {
            with(CacheDataHolder.cache) {
                find { it.key == key }?.let { remove(it) }
            }
        }
    }
}

/**
 * Item to be cached.
 *
 * @property key item unique key.
 * @property data data to be cached, any type of data is acceptable.
 * @property durationInMinutes time in minutes that this item will be available in cache. Null to skip the validation.
 * @property expiresAt date when the item will be expired, filled by the CacheManager.
 */
data class CacheItem(
    val key: String,
    val data: Any,
    val durationInMinutes: Int? = null,
    var expiresAt: Date? = null
)

/**
 * Holder for the cached items.
 */
object CacheDataHolder {
    val cache = mutableListOf<CacheItem>()
}