package ru.constructor.handbook.database.projects

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.constructor.handbook.database.entity.*

@Dao
public interface ProjectDao {
    @Query("SELECT * FROM projects WHERE (:includeArchived OR archived = 0) ORDER BY updated_at DESC") public fun observeProjects(includeArchived: Boolean): Flow<List<ProjectEntity>>
    @Query("SELECT * FROM projects WHERE id = :projectId") public fun observeProject(projectId: String): Flow<ProjectEntity?>
    @Query("SELECT * FROM assemblies WHERE project_id = :projectId ORDER BY name") public fun observeAssemblies(projectId: String): Flow<List<AssemblyEntity>>
    @Query("SELECT * FROM parts WHERE project_id = :projectId ORDER BY updated_at DESC") public fun observeParts(projectId: String): Flow<List<PartEntity>>
    @Query("SELECT measurements.* FROM measurements INNER JOIN parts ON parts.id = measurements.part_id WHERE parts.project_id = :projectId ORDER BY measured_at DESC") public fun observeMeasurements(projectId: String): Flow<List<MeasurementEntity>>
    @Query("SELECT * FROM attachments WHERE project_id = :projectId ORDER BY created_at DESC") public fun observeAttachments(projectId: String): Flow<List<AttachmentEntity>>
    @Query("SELECT * FROM notes WHERE project_id = :projectId ORDER BY updated_at DESC") public fun observeNotes(projectId: String): Flow<List<NoteEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertProject(entity: ProjectEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertAssembly(entity: AssemblyEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertPart(entity: PartEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertMeasurement(entity: MeasurementEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertAttachment(entity: AttachmentEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertNote(entity: NoteEntity)
    @Query("UPDATE projects SET name = :name, description = :description, updated_at = :updatedAt WHERE id = :projectId") public suspend fun updateProject(projectId: String, name: String, description: String?, updatedAt: String)
    @Query("UPDATE projects SET archived = 1, updated_at = :updatedAt WHERE id = :projectId") public suspend fun archiveProject(projectId: String, updatedAt: String)
    @Query("UPDATE projects SET updated_at = :updatedAt WHERE id = :projectId") public suspend fun touchProject(projectId: String, updatedAt: String)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertFavorite(entity: FavoriteEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertRecent(entity: RecentItemEntity)
    @Query("SELECT * FROM favorites ORDER BY created_at DESC") public fun observeFavorites(): Flow<List<FavoriteEntity>>
    @Query("SELECT * FROM recent_items ORDER BY opened_at DESC LIMIT :limit") public fun observeRecent(limit: Int): Flow<List<RecentItemEntity>>
}
