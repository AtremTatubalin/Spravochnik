package ru.constructor.handbook.reference.model

public data class Source(
    val id: String,
    val title: String,
    val sourceType: String,
    val url: String?,
    val edition: String?,
    val accessedAt: String?,
    val verificationStatus: String,
    val licenseScope: String?,
)

public data class Standard(
    val id: String,
    val designation: String,
    val titleRu: String,
    val status: String,
    val effectiveFrom: String?,
    val effectiveTo: String?,
    val earlyApplicationAllowed: Boolean,
    val sourceId: String,
    val verifiedAsOf: String,
    val scope: String?,
    val notes: List<String>,
)

public data class StandardChange(
    val id: String,
    val standardId: String,
    val changeType: String,
    val designation: String?,
    val publication: String?,
    val registeredAt: String?,
    val effectiveFrom: String?,
    val sourceId: String?,
)
