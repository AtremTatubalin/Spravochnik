package ru.constructor.handbook.database.projects

import androidx.room.withTransaction
import java.time.Clock
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.constructor.handbook.database.HandbookDatabase
import ru.constructor.handbook.database.entity.*
import ru.constructor.handbook.projects.model.*
import ru.constructor.handbook.projects.repository.ProjectRepository

public class RoomProjectRepository(private val database: HandbookDatabase, private val clock: Clock = Clock.systemUTC()) : ProjectRepository {
    private val dao = database.projectDao()
    override fun observeProjects(includeArchived: Boolean): Flow<List<Project>> = dao.observeProjects(includeArchived).mapList { it.toDomain() }
    override fun observeProjectDetail(projectId: String): Flow<ProjectDetail?> {
        val header = combine(dao.observeProject(projectId), dao.observeAssemblies(projectId), dao.observeParts(projectId)) { project, assemblies, parts ->
            Triple(project, assemblies, parts)
        }
        val body = combine(dao.observeMeasurements(projectId), dao.observeAttachments(projectId), dao.observeNotes(projectId)) { measurements, attachments, notes ->
            Triple(measurements, attachments, notes)
        }
        return combine(header, body) { (project, assemblies, parts), (measurements, attachments, notes) ->
            project?.let {
                ProjectDetail(
                    project = it.toDomain(),
                    assemblies = assemblies.map(AssemblyEntity::toDomain),
                    parts = parts.map(PartEntity::toDomain),
                    measurements = measurements.map(MeasurementEntity::toDomain),
                    attachments = attachments.map(AttachmentEntity::toDomain),
                    notes = notes.map(NoteEntity::toDomain),
                )
            }
        }
    }
    override suspend fun createProject(name: String, description: String?): String = write { id -> dao.upsertProject(ProjectEntity(id, name.trim(), description?.trim()?.ifBlank { null }, now(), now())) }
    override suspend fun updateProject(projectId: String, name: String, description: String?) { dao.updateProject(projectId, name.trim(), description?.trim()?.ifBlank { null }, now()) }
    override suspend fun archiveProject(projectId: String) { dao.archiveProject(projectId, now()) }
    override suspend fun addAssembly(projectId: String, parentAssemblyId: String?, name: String, notes: String?): String = write { id -> dao.upsertAssembly(AssemblyEntity(id, projectId, parentAssemblyId, name.trim(), notes)); dao.touchProject(projectId, now()) }
    override suspend fun addPart(projectId: String, assemblyId: String?, name: String, partType: String?, designation: String?, materialId: String?, notes: String?): String = write { id -> dao.upsertPart(PartEntity(id, projectId, assemblyId, name.trim(), partType, designation, materialId, notes, now(), now())); dao.touchProject(projectId, now()) }
    override suspend fun addMeasurement(partId: String, key: String, value: Double, unit: String, uncertainty: Double?, method: String?, instrument: String?, measuredAt: String?, notes: String?): String = write { id -> dao.upsertMeasurement(MeasurementEntity(id, partId, key, value, unit, uncertainty, method, instrument, measuredAt, notes)) }
    override suspend fun addPhotoAttachment(projectId: String, partId: String?, uri: String, mimeType: String?, caption: String?, checksum: String?): String = write { id -> dao.upsertAttachment(AttachmentEntity(id, projectId, partId, uri, mimeType, caption, checksum, now())); dao.touchProject(projectId, now()) }
    override suspend fun addNote(projectId: String, partId: String?, body: String): String = write { id -> dao.upsertNote(NoteEntity(id, projectId, partId, body, now(), now())); dao.touchProject(projectId, now()) }
    private suspend fun write(block: suspend (String) -> Unit): String { val id = UUID.randomUUID().toString(); database.withTransaction { block(id) }; return id }
    private fun now(): String = Instant.now(clock).toString()
}

private inline fun <T, R> Flow<List<T>>.mapList(crossinline mapper: (T) -> R): Flow<List<R>> = this.map { rows: List<T> -> rows.map(mapper) }
private fun ProjectEntity.toDomain() = Project(id, name, description, created_at, updated_at, archived)
private fun AssemblyEntity.toDomain() = Assembly(id, project_id, parent_assembly_id, name, notes)
private fun PartEntity.toDomain() = Part(id, project_id, assembly_id, name, part_type, designation, material_id, notes, created_at, updated_at)
private fun MeasurementEntity.toDomain() = Measurement(id, part_id, key, value, unit, uncertainty, method, instrument, measured_at, notes)
private fun AttachmentEntity.toDomain() = PhotoAttachment(id, project_id, part_id, uri, mime_type, caption, checksum, created_at)
private fun NoteEntity.toDomain() = ProjectNote(id, project_id, part_id, body, created_at, updated_at)
