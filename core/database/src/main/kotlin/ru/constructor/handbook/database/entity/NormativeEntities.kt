package ru.constructor.handbook.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "datasets")
public data class DatasetEntity(
    @PrimaryKey val id: String,
    val version: String,
    @ColumnInfo(name = "schema_version") val schemaVersion: Int,
    @ColumnInfo(name = "verified_as_of") val verifiedAsOf: String?,
    val checksum: String,
    @ColumnInfo(name = "imported_at") val importedAt: String,
    val status: String,
)

@Entity(tableName = "sources")
public data class SourceEntity(
    @PrimaryKey val id: String,
    val title: String,
    @ColumnInfo(name = "source_type") val sourceType: String,
    val url: String?,
    val edition: String?,
    @ColumnInfo(name = "accessed_at") val accessedAt: String?,
    @ColumnInfo(name = "verification_status") val verificationStatus: String,
    @ColumnInfo(name = "license_scope") val licenseScope: String?,
)

@Entity(
    tableName = "standards",
    foreignKeys = [ForeignKey(SourceEntity::class, ["id"], ["source_id"])],
    indices = [Index(value = ["designation"], unique = true), Index("source_id")],
)
public data class StandardEntity(
    @PrimaryKey val id: String,
    val designation: String,
    @ColumnInfo(name = "title_ru") val titleRu: String,
    val status: String,
    @ColumnInfo(name = "effective_from") val effectiveFrom: String?,
    @ColumnInfo(name = "effective_to") val effectiveTo: String?,
    @ColumnInfo(name = "early_application_allowed") val earlyApplicationAllowed: Boolean,
    @ColumnInfo(name = "source_id") val sourceId: String,
    @ColumnInfo(name = "verified_as_of") val verifiedAsOf: String,
    val scope: String?,
    @ColumnInfo(name = "notes_json") val notesJson: String,
)

@Entity(
    tableName = "standard_changes",
    foreignKeys = [
        ForeignKey(StandardEntity::class, ["id"], ["standard_id"]),
        ForeignKey(SourceEntity::class, ["id"], ["source_id"]),
    ],
    indices = [Index("standard_id"), Index("source_id")],
)
public data class StandardChangeEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "standard_id") val standardId: String,
    @ColumnInfo(name = "change_type") val changeType: String,
    val designation: String?,
    val publication: String?,
    @ColumnInfo(name = "registered_at") val registeredAt: String?,
    @ColumnInfo(name = "effective_from") val effectiveFrom: String?,
    @ColumnInfo(name = "source_id") val sourceId: String?,
)
