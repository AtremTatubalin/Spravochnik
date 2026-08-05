package ru.constructor.handbook.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.constructor.handbook.database.importer.SeedChecksumException
import ru.constructor.handbook.database.importer.SeedImportResult
import ru.constructor.handbook.database.importer.SeedManifestImporter

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
public class SeedManifestImporterTest {
    private lateinit var context: Context
    private lateinit var database: HandbookDatabase

    @Before public fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, HandbookDatabase::class.java)
            // Robolectric's framework SQLite is built without FTS5. Production uses
            // HandbookDatabase.CREATE_SEARCH_INDEX; this compatible table exercises import transactions.
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    db.execSQL("CREATE TABLE search_index(record_type TEXT, record_id TEXT, title TEXT, designation TEXT, aliases TEXT)")
                }
            })
            .allowMainThreadQueries()
            .build()
    }

    @After public fun tearDown() = database.close()

    @Test public fun `initial import inserts supplied metadata and search index`() = runBlocking {
        val result = importer().import()
        assertEquals(SeedImportResult.Imported(2), result)
        assertEquals(20, database.normativeDao().sourceCount())
        assertEquals(15, database.normativeDao().standardCount())
        assertEquals(15, database.query("SELECT count(*) FROM search_index", emptyArray()).use { it.moveToFirst(); it.getInt(0) })
    }

    @Test public fun `repeated import is idempotent`() = runBlocking {
        val importer = importer()
        importer.import()
        assertEquals(SeedImportResult.AlreadyCurrent, importer.import())
        assertEquals(20, database.normativeDao().sourceCount())
        assertEquals(15, database.normativeDao().standardCount())
    }

    @Test public fun `invalid checksum leaves database unchanged`() = runBlocking {
        val clean = assetReader()
        val corrupted = SeedManifestImporter(database, readAsset = { path ->
            if (path.endsWith("sources.json")) clean(path) + byteArrayOf(0) else clean(path)
        })
        assertThrows(SeedChecksumException::class.java) { runBlocking { corrupted.import() } }
        assertEquals(0, database.normativeDao().sourceCount())
        assertEquals(0, database.normativeDao().standardCount())
    }

    private fun importer() = SeedManifestImporter(database, readAsset = assetReader(), now = { "2026-08-04T00:00:00Z" })

    private fun assetReader(): (String) -> ByteArray = { path ->
        context.assets.open(path).use { it.readBytes() }
    }
}
