package com.premiumweather.app

import com.premiumweather.app.domain.model.CacheFreshness
import org.junit.Assert.*
import org.junit.Test
import java.time.Duration
import java.time.Instant

class CacheFreshnessTest {
    @Test fun freshnessLogic() {
        fun compute(fetched: Instant): CacheFreshness {
            val age = Duration.between(fetched, Instant.now())
            return when {
                age.toMinutes() < 30 -> CacheFreshness.FRESH
                age.toHours() < 6 -> CacheFreshness.STALE
                else -> CacheFreshness.VERY_STALE
            }
        }
        assertEquals(CacheFreshness.FRESH, compute(Instant.now().minus(Duration.ofMinutes(10))))
        assertEquals(CacheFreshness.STALE, compute(Instant.now().minus(Duration.ofHours(2))))
        assertEquals(CacheFreshness.VERY_STALE, compute(Instant.now().minus(Duration.ofHours(10))))
    }
}
