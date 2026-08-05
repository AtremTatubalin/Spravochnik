package ru.constructor.handbook.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.constructor.handbook.database.dao.NormativeDao
import ru.constructor.handbook.database.projects.ProjectDao
import ru.constructor.handbook.database.entity.*

@Database(
    entities = [DatasetEntity::class, SourceEntity::class, StandardEntity::class, StandardChangeEntity::class, ProjectEntity::class, AssemblyEntity::class, PartEntity::class, MeasurementEntity::class, AttachmentEntity::class, NoteEntity::class, FavoriteEntity::class, RecentItemEntity::class],
    version = 3,
    exportSchema = true,
)
public abstract class HandbookDatabase : RoomDatabase() {
    public abstract fun normativeDao(): NormativeDao
    public abstract fun projectDao(): ProjectDao

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

        public val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                PROJECT_TABLE_SQL.forEach(db::execSQL)
            }
        }

        public val PROJECT_TABLE_SQL: List<String> = listOf(
            "CREATE TABLE IF NOT EXISTS `projects` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `created_at` TEXT NOT NULL, `updated_at` TEXT NOT NULL, `archived` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`id`))",
            "CREATE TABLE IF NOT EXISTS `assemblies` (`id` TEXT NOT NULL, `project_id` TEXT NOT NULL, `parent_assembly_id` TEXT, `name` TEXT NOT NULL, `notes` TEXT, PRIMARY KEY(`id`), FOREIGN KEY(`project_id`) REFERENCES `projects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)",
            "CREATE TABLE IF NOT EXISTS `parts` (`id` TEXT NOT NULL, `project_id` TEXT NOT NULL, `assembly_id` TEXT, `name` TEXT NOT NULL, `part_type` TEXT, `designation` TEXT, `material_id` TEXT, `notes` TEXT, `created_at` TEXT NOT NULL, `updated_at` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`project_id`) REFERENCES `projects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)",
            "CREATE TABLE IF NOT EXISTS `measurements` (`id` TEXT NOT NULL, `part_id` TEXT NOT NULL, `key` TEXT NOT NULL, `value` REAL NOT NULL, `unit` TEXT NOT NULL, `uncertainty` REAL, `method` TEXT, `instrument` TEXT, `measured_at` TEXT, `notes` TEXT, PRIMARY KEY(`id`), FOREIGN KEY(`part_id`) REFERENCES `parts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)",
            "CREATE TABLE IF NOT EXISTS `attachments` (`id` TEXT NOT NULL, `project_id` TEXT NOT NULL, `part_id` TEXT, `uri` TEXT NOT NULL, `mime_type` TEXT, `caption` TEXT, `checksum` TEXT, `created_at` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`project_id`) REFERENCES `projects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)",
            "CREATE TABLE IF NOT EXISTS `notes` (`id` TEXT NOT NULL, `project_id` TEXT NOT NULL, `part_id` TEXT, `body` TEXT NOT NULL, `created_at` TEXT NOT NULL, `updated_at` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`project_id`) REFERENCES `projects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)",
            "CREATE TABLE IF NOT EXISTS `favorites` (`record_type` TEXT NOT NULL, `record_id` TEXT NOT NULL, `created_at` TEXT NOT NULL, PRIMARY KEY(`record_type`, `record_id`))",
            "CREATE TABLE IF NOT EXISTS `recent_items` (`record_type` TEXT NOT NULL, `record_id` TEXT NOT NULL, `title` TEXT NOT NULL, `designation` TEXT, `opened_at` TEXT NOT NULL, PRIMARY KEY(`record_type`, `record_id`))",
            "CREATE INDEX IF NOT EXISTS `index_assemblies_project_id` ON `assemblies` (`project_id`)",
            "CREATE INDEX IF NOT EXISTS `index_parts_project_id` ON `parts` (`project_id`)",
            "CREATE INDEX IF NOT EXISTS `index_parts_assembly_id` ON `parts` (`assembly_id`)",
            "CREATE INDEX IF NOT EXISTS `index_measurements_part_id` ON `measurements` (`part_id`)",
            "CREATE INDEX IF NOT EXISTS `index_attachments_project_id` ON `attachments` (`project_id`)",
            "CREATE INDEX IF NOT EXISTS `index_attachments_part_id` ON `attachments` (`part_id`)",
            "CREATE INDEX IF NOT EXISTS `index_notes_project_id` ON `notes` (`project_id`)",
            "CREATE INDEX IF NOT EXISTS `index_notes_part_id` ON `notes` (`part_id`)",
        )

        public const val SEARCH_INDEX_SQL: String = "CREATE VIRTUAL TABLE IF NOT EXISTS `search_index` USING FTS5(`record_type`, `record_id` UNINDEXED, `title`, `designation`, `aliases`, content='')"
    }
}
