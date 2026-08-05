package ru.constructor.handbook.reverse

import kotlinx.serialization.Serializable

@Serializable
public data class ReverseWizardDefinition(
    val id: String,
    val titleRu: String,
    val measurements: List<ReverseMeasurementSpec>,
    val candidateProvider: String,
    val scoring: ReverseScoringSpec,
    val stopConditions: List<String>,
    val warnings: List<String>,
)

@Serializable
public data class ReverseMeasurementSpec(
    val key: String,
    val unit: String,
    val weight: Double,
    val required: Boolean,
)

@Serializable
public data class ReverseScoringSpec(
    val mode: String,
    val defaultToleranceMm: Double? = null,
    val exactBonus: Double? = null,
    val wearPenalty: Double? = null,
    val outsideDiameterWearPenalty: Double? = null,
    val wearAdjustedFields: List<String> = emptyList(),
    val preferredFields: List<String> = emptyList(),
    val confirmationFields: List<String> = emptyList(),
)

public data class ReverseMeasurement(
    val key: String,
    val value: Double? = null,
    val text: String? = null,
    val unit: String,
    val uncertainty: Double = 0.0,
    val worn: Boolean = false,
)

public data class ReverseCandidate(
    val id: String,
    val title: String,
    val values: Map<String, Double>,
    val textValues: Map<String, String> = emptyMap(),
    val sourceId: String? = null,
)

public sealed interface ReverseWizardResult {
    public val wizardId: String
    public val warnings: List<String>
    public val nextBestMeasurement: String?

    public data class Scored(
        override val wizardId: String,
        val candidates: List<ScoredReverseCandidate>,
        val contradictions: List<ReverseContradiction>,
        override val nextBestMeasurement: String?,
        override val warnings: List<String>,
    ) : ReverseWizardResult

    public data class RequiresStandardTable(
        override val wizardId: String,
        val reasonRu: String,
        override val nextBestMeasurement: String?,
        override val warnings: List<String>,
    ) : ReverseWizardResult
}

public data class ScoredReverseCandidate(
    val candidate: ReverseCandidate,
    val confidence: Double,
    val reasons: List<String>,
)

public data class ReverseContradiction(
    val key: String,
    val messageRu: String,
)
