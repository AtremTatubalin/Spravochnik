package ru.constructor.handbook.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
public data class ProjectEntity(@PrimaryKey val id: String, val name: String, val description: String?, val created_at: String, val updated_at: String, val archived: Boolean = false)

@Entity(tableName = "assemblies", indices = [Index("project_id")], foreignKeys = [ForeignKey(ProjectEntity::class, ["id"], ["project_id"], onDelete = ForeignKey.CASCADE)])
public data class AssemblyEntity(@PrimaryKey val id: String, val project_id: String, val parent_assembly_id: String?, val name: String, val notes: String?)

@Entity(tableName = "parts", indices = [Index("project_id"), Index("assembly_id")], foreignKeys = [ForeignKey(ProjectEntity::class, ["id"], ["project_id"], onDelete = ForeignKey.CASCADE)])
public data class PartEntity(@PrimaryKey val id: String, val project_id: String, val assembly_id: String?, val name: String, val part_type: String?, val designation: String?, val material_id: String?, val notes: String?, val created_at: String, val updated_at: String)

@Entity(tableName = "measurements", indices = [Index("part_id")], foreignKeys = [ForeignKey(PartEntity::class, ["id"], ["part_id"], onDelete = ForeignKey.CASCADE)])
public data class MeasurementEntity(@PrimaryKey val id: String, val part_id: String, val key: String, val value: Double, val unit: String, val uncertainty: Double?, val method: String?, val instrument: String?, val measured_at: String?, val notes: String?)

@Entity(tableName = "attachments", indices = [Index("project_id"), Index("part_id")], foreignKeys = [ForeignKey(ProjectEntity::class, ["id"], ["project_id"], onDelete = ForeignKey.CASCADE)])
public data class AttachmentEntity(@PrimaryKey val id: String, val project_id: String, val part_id: String?, val uri: String, val mime_type: String?, val caption: String?, val checksum: String?, val created_at: String)

@Entity(tableName = "notes", indices = [Index("project_id"), Index("part_id")], foreignKeys = [ForeignKey(ProjectEntity::class, ["id"], ["project_id"], onDelete = ForeignKey.CASCADE)])
public data class NoteEntity(@PrimaryKey val id: String, val project_id: String, val part_id: String?, val body: String, val created_at: String, val updated_at: String)

@Entity(tableName = "favorites", primaryKeys = ["record_type", "record_id"])
public data class FavoriteEntity(val record_type: String, val record_id: String, val created_at: String)

@Entity(tableName = "recent_items", primaryKeys = ["record_type", "record_id"])
public data class RecentItemEntity(val record_type: String, val record_id: String, val title: String, val designation: String?, val opened_at: String)
