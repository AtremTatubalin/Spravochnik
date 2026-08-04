package ru.constructor.handbook.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.constructor.handbook.database.dao.NormativeDao
import ru.constructor.handbook.database.entity.*

@Database(
    entities = [DatasetEntity::class, SourceEntity::class, StandardEntity::class, StandardChangeEntity::class],
    version = 2,
    exportSchema = true,
)
public abstract class HandbookDatabase : RoomDatabase() {
    public abstract fun normativeDao(): NormativeDao

    public companion object {
        public val CREATE_SEARCH_INDEX: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                db.execSQL(SEARCH_INDEX_SQL)
            }
        }

        public val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(SEARCH_INDEX_SQL)
            }
        }

        public const val SEARCH_INDEX_SQL: String = "CREATE VIRTUAL TABLE IF NOT EXISTS `search_index` USING FTS5(`record_type`, `record_id` UNINDEXED, `title`, `designation`, `aliases`, content='')"
    }
}
