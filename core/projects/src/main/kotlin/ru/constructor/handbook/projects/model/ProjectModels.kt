package ru.constructor.handbook.projects.model

public data class Project(
    val id: String,
    val name: String,
    val description: String?,
    val createdAt: String,
    val updatedAt: String,
    val archived: Boolean,
)

public data class Assembly(
    val id: String,
    val projectId: String,
    val parentAssemblyId: String?,
    val name: String,
    val notes: String?,
)

public data class Part(
    val id: String,
    val projectId: String,
    val assemblyId: String?,
    val name: String,
    val partType: String?,
    val designation: String?,
    val materialId: String?,
    val notes: String?,
    val createdAt: String,
    val updatedAt: String,
)

public data class Measurement(
    val id: String,
    val partId: String,
    val key: String,
    val value: Double,
    val unit: String,
    val uncertainty: Double?,
    val method: String?,
    val instrument: String?,
    val measuredAt: String?,
    val notes: String?,
)

public data class PhotoAttachment(
    val id: String,
    val projectId: String,
    val partId: String?,
    val uri: String,
    val mimeType: String?,
    val caption: String?,
    val checksum: String?,
    val createdAt: String,
)

public data class ProjectNote(
    val id: String,
    val projectId: String,
    val partId: String?,
    val body: String,
    val createdAt: String,
    val updatedAt: String,
)

public data class ProjectDetail(
    val project: Project,
    val assemblies: List<Assembly>,
    val parts: List<Part>,
    val measurements: List<Measurement>,
    val attachments: List<PhotoAttachment>,
    val notes: List<ProjectNote>,
)
