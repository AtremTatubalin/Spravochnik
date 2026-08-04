package ru.constructor.handbook.database

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

public class HandbookMigrationTest {
    @Test public fun `migration one to two only adds contentless fts5 index`() {
        val sql = HandbookDatabase.SEARCH_INDEX_SQL.uppercase()
        assertTrue(sql.contains("CREATE VIRTUAL TABLE IF NOT EXISTS"))
        assertTrue(sql.contains("USING FTS5"))
        assertTrue(sql.contains("CONTENT=''"))
        assertFalse(sql.contains("DROP TABLE"))
        assertFalse(sql.contains("DELETE FROM"))
    }
}
