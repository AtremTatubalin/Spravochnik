package ru.constructor.handbook.reference.search

import org.junit.Assert.assertEquals
import org.junit.Test

public class SearchNormalizerTest {
    @Test public fun `normalizes separators case and latin x`() {
        assertEquals("40х", SearchNormalizer.normalize(" 40-X "))
    }

    @Test public fun `normalizes transliterated kh`() {
        assertEquals("12х18н10т", SearchNormalizer.normalize("12Kh18Н10Т"))
    }
}
