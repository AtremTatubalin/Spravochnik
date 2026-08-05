package ru.constructor.handbook.projects.repository

import kotlinx.coroutines.flow.Flow
import ru.constructor.handbook.projects.model.*

public interface ProjectRepository {
    public fun observeProjects(includeArchived: Boolean = false): Flow<List<Project>>
    public fun observeProjectDetail(projectId: String): Flow<ProjectDetail?>
    public suspend fun createProject(name: String, description: String?): String
    public suspend fun updateProject(projectId: String, name: String, description: String?)
    public suspend fun archiveProject(projectId: String)
    public suspend fun addAssembly(projectId: String, parentAssemblyId: String?, name: String, notes: String?): String
    public suspend fun addPart(projectId: String, assemblyId: String?, name: String, partType: String?, designation: String?, materialId: String?, notes: String?): String
    public suspend fun addMeasurement(partId: String, key: String, value: Double, unit: String, uncertainty: Double?, method: String?, instrument: String?, measuredAt: String?, notes: String?): String
    public suspend fun addPhotoAttachment(projectId: String, partId: String?, uri: String, mimeType: String?, caption: String?, checksum: String?): String
    public suspend fun addNote(projectId: String, partId: String?, body: String): String
}
