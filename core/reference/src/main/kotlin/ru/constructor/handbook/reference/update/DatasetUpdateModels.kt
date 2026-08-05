package ru.constructor.handbook.reference.update

public data class DatasetVersion(
    val datasetId: String,
    val version: String,
    val importedAt: String,
    val verificationStatus: String,
)

public data class UpdatePackageEntry(
    val datasetId: String,
    val version: String,
    val checksumSha256: String,
    val verified: Boolean,
)

public data class UpdateDryRunResult(
    val entriesToImport: List<UpdatePackageEntry>,
    val blockedEntries: List<UpdatePackageEntry>,
    val warnings: List<String>,
) {
    public val canImport: Boolean get() = blockedEntries.isEmpty()
}

public object DatasetUpdatePlanner {
    public fun dryRun(current: List<DatasetVersion>, update: List<UpdatePackageEntry>): UpdateDryRunResult {
        val currentVersions = current.associate { it.datasetId to it.version }
        val blocked = update.filter { !it.verified || it.checksumSha256.length != 64 }
        val importable = update.filter { it !in blocked && currentVersions[it.datasetId] != it.version }
        val warnings = blocked.map { "${it.datasetId}: пакет заблокирован — требуется verified=true и SHA-256" }
        return UpdateDryRunResult(importable, blocked, warnings)
    }

    public fun importPlan(dryRun: UpdateDryRunResult): List<DatasetVersion> {
        require(dryRun.canImport) { "Нельзя импортировать пакет с заблокированными наборами" }
        return dryRun.entriesToImport.map { DatasetVersion(it.datasetId, it.version, "pending_transaction", "verified") }
    }
}
