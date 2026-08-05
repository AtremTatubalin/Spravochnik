package ru.constructor.handbook.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.constructor.handbook.database.dao.NormativeDao
import ru.constructor.handbook.database.entity.SourceEntity
import ru.constructor.handbook.database.entity.StandardChangeEntity
import ru.constructor.handbook.database.entity.StandardEntity
import ru.constructor.handbook.reference.model.Source
import ru.constructor.handbook.reference.model.Standard
import ru.constructor.handbook.reference.model.StandardChange
import ru.constructor.handbook.reference.repository.NormativeRepository
import ru.constructor.handbook.reference.search.SearchNormalizer

public class RoomNormativeRepository(
    private val dao: NormativeDao,
    private val json: Json = Json,
) : NormativeRepository {
    override fun observeSources(): Flow<List<Source>> = dao.observeSources().map { rows -> rows.map(SourceEntity::toDomain) }
    override fun observeStandards(): Flow<List<Standard>> = dao.observeStandards().map { rows -> rows.map(::standard) }
    override fun observeChanges(standardId: String): Flow<List<StandardChange>> =
        dao.observeChanges(standardId).map { rows -> rows.map(StandardChangeEntity::toDomain) }

    override suspend fun searchStandards(query: String): List<Standard> {
        val normalized = SearchNormalizer.normalize(query)
        if (normalized.isBlank()) return emptyList()
        val match = "${escapeFts(normalized)}*"
        return dao.searchStandards(
            SimpleSQLiteQuery(
                "SELECT standards.* FROM standards JOIN search_index ON standards.id = search_index.record_id WHERE search_index MATCH ? ORDER BY standards.designation",
                arrayOf(match),
            ),
        ).map(::standard)
    }

    private fun standard(row: StandardEntity): Standard = Standard(
        row.id, row.designation, row.titleRu, row.status, row.effectiveFrom, row.effectiveTo,
        row.earlyApplicationAllowed, row.sourceId, row.verifiedAsOf, row.scope,
        json.decodeFromString(row.notesJson),
    )

    private fun escapeFts(value: String): String = '"' + value.replace("\"", "\"\"") + '"'
}

private fun SourceEntity.toDomain() = Source(id, title, sourceType, url, edition, accessedAt, verificationStatus, licenseScope)
private fun StandardChangeEntity.toDomain() = StandardChange(id, standardId, changeType, designation, publication, registeredAt, effectiveFrom, sourceId)
