package ru.constructor.handbook.database.importer

import android.content.res.AssetManager
import androidx.room.withTransaction
import java.security.MessageDigest
import java.time.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.constructor.handbook.database.HandbookDatabase
import ru.constructor.handbook.database.entity.*
import ru.constructor.handbook.reference.search.SearchNormalizer

public sealed interface SeedImportResult {
    public data class Imported(val datasetCount: Int) : SeedImportResult
    public data object AlreadyCurrent : SeedImportResult
}

public class SeedChecksumException(message: String) : IllegalArgumentException(message)

public class SeedManifestImporter(
    private val database: HandbookDatabase,
    private val readAsset: (String) -> ByteArray,
    private val json: Json = Json { ignoreUnknownKeys = true },
    private val now: () -> String = { Instant.now().toString() },
) {
    public constructor(
        database: HandbookDatabase,
        assets: AssetManager,
        json: Json = Json { ignoreUnknownKeys = true },
        now: () -> String = { Instant.now().toString() },
    ) : this(database, { path -> assets.open(path).use { it.readBytes() } }, json, now)

    public suspend fun import(assetDirectory: String = "seeds"): SeedImportResult {
        val manifestBytes = readAsset("$assetDirectory/manifest.json")
        val manifest = json.decodeFromString<SeedManifest>(manifestBytes.decodeToString())
        val verifiedFiles = manifest.datasets.associateWith { item ->
            val bytes = readAsset("$assetDirectory/${item.file}")
            val actual = sha256(bytes)
            if (!actual.equals(item.sha256, ignoreCase = true)) {
                throw SeedChecksumException("Checksum mismatch for ${item.id}: expected ${item.sha256}, got $actual")
            }
            bytes
        }
        val manifestChecksum = sha256(manifestBytes)
        if (database.normativeDao().dataset(MANIFEST_DATASET_ID)?.checksum == manifestChecksum) {
            return SeedImportResult.AlreadyCurrent
        }

        val sources = decodeSources(verifiedFiles.required("sources"))
        val standards = decodeStandards(verifiedFiles.required("standards"))
        val importedAt = now()

        database.withTransaction {
            val dao = database.normativeDao()
            dao.upsertSources(sources)
            dao.upsertStandards(standards)
            rebuildSearchIndex(standards)
            dao.upsertDatasets(manifest.datasets.filter { it.id in SUPPORTED_DATASETS }.map { item ->
                DatasetEntity(item.id, manifest.packageVersion, manifest.schemaVersion, manifest.verifiedAsOf, item.sha256, importedAt, item.status)
            } + DatasetEntity(MANIFEST_DATASET_ID, manifest.packageVersion, manifest.schemaVersion, manifest.verifiedAsOf, manifestChecksum, importedAt, "verified_manifest"))
        }
        return SeedImportResult.Imported(SUPPORTED_DATASETS.size)
    }

    private fun decodeSources(bytes: ByteArray): List<SourceEntity> =
        json.decodeFromString<List<SourceSeed>>(bytes.decodeToString()).map { it.toEntity() }

    private fun decodeStandards(bytes: ByteArray): List<StandardEntity> =
        json.decodeFromString<List<StandardSeed>>(bytes.decodeToString()).map { it.toEntity(json) }

    private fun indexed(value: String): String = listOf(value, SearchNormalizer.normalize(value)).joinToString(" ")

    private fun rebuildSearchIndex(standards: List<StandardEntity>) {
        val sqlite = database.openHelper.writableDatabase
        sqlite.execSQL("DELETE FROM search_index")
        val statement = sqlite.compileStatement(
            "INSERT INTO search_index(record_type, record_id, title, designation, aliases) VALUES (?, ?, ?, ?, ?)",
        )
        standards.forEach { standard ->
            statement.clearBindings()
            statement.bindString(1, "standard")
            statement.bindString(2, standard.id)
            statement.bindString(3, indexed(standard.titleRu))
            statement.bindString(4, indexed(standard.designation))
            statement.bindString(5, "")
            statement.executeInsert()
        }
    }

    private fun Map<SeedDataset, ByteArray>.required(id: String): ByteArray =
        entries.firstOrNull { it.key.id == id }?.value ?: error("Required seed dataset is absent: $id")

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes).joinToString("") { "%02x".format(it) }

    private companion object {
        const val MANIFEST_DATASET_ID = "seed_manifest"
        val SUPPORTED_DATASETS = setOf("sources", "standards")
    }
}

@Serializable
private data class SeedManifest(
    val packageVersion: String,
    val schemaVersion: Int,
    val verifiedAsOf: String? = null,
    val datasets: List<SeedDataset>,
)

@Serializable
private data class SeedDataset(val id: String, val file: String, val status: String, val sha256: String)

@Serializable
private data class SourceSeed(
    val sourceId: String,
    val title: String,
    val sourceType: String,
    val url: String? = null,
    val edition: String? = null,
    val accessedAt: String? = null,
    val verificationStatus: String,
    val licenseScope: String? = null,
) {
    fun toEntity() = SourceEntity(sourceId, title, sourceType, url, edition, accessedAt, verificationStatus, licenseScope)
}

@Serializable
private data class StandardSeed(
    val id: String,
    val designation: String,
    val titleRu: String,
    val status: String,
    val effectiveFrom: String? = null,
    val effectiveTo: String? = null,
    val earlyApplicationAllowed: Boolean,
    val sourceId: String,
    val verifiedAsOf: String,
    val scope: String? = null,
    val notes: List<String> = emptyList(),
) {
    fun toEntity(json: Json) = StandardEntity(id, designation, titleRu, status, effectiveFrom, effectiveTo, earlyApplicationAllowed, sourceId, verifiedAsOf, scope, json.encodeToString(notes))
}
