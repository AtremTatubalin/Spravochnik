package ru.constructor.handbook.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import ru.constructor.handbook.database.entity.*

@Dao
public interface NormativeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertDatasets(rows: List<DatasetEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertSources(rows: List<SourceEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertStandards(rows: List<StandardEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) public suspend fun upsertChanges(rows: List<StandardChangeEntity>)
    @Query("SELECT * FROM datasets WHERE id = :id") public suspend fun dataset(id: String): DatasetEntity?
    @Query("SELECT * FROM sources ORDER BY title") public fun observeSources(): Flow<List<SourceEntity>>
    @Query("SELECT * FROM standards ORDER BY designation") public fun observeStandards(): Flow<List<StandardEntity>>
    @Query("SELECT * FROM standard_changes WHERE standard_id = :standardId ORDER BY effective_from") public fun observeChanges(standardId: String): Flow<List<StandardChangeEntity>>
    @RawQuery(observedEntities = [StandardEntity::class])
    public suspend fun searchStandards(query: SupportSQLiteQuery): List<StandardEntity>
    @Query("SELECT COUNT(*) FROM sources") public suspend fun sourceCount(): Int
    @Query("SELECT COUNT(*) FROM standards") public suspend fun standardCount(): Int
}
