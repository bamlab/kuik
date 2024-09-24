package com.theodo.apps.kuik

import kotlin.test.assertContains
import kotlin.test.assertEquals

fun assertListEquals(
    expected: List<String>,
    actual: List<String>,
) {
    assertEquals(expected.count(), actual.count())
    expected.forEach {
        assertContains(actual, it, "Expected $it not found in $actual")
    }
    actual.forEach {
        assertContains(expected, it, "Unexpected $it found in $actual")
    }
}
