package ru.constructor.handbook.feature.settings

import org.junit.Assert.assertTrue
import org.junit.Test

class DatasetVersionPageStateTest {
    @Test fun datasetVersionPageShowsDryRunStatus() {
        assertTrue(DatasetVersionPageState.isNotEmpty())
        assertTrue(DatasetVersionPageState.all { it.updateStatus.contains("dry-run") })
    }
}
