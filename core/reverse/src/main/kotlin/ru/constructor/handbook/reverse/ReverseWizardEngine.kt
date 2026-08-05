package ru.constructor.handbook.reverse

import kotlin.math.abs
import kotlin.math.max

public object ReverseWizardEngine {
    public fun evaluate(
        definition: ReverseWizardDefinition,
        measurements: List<ReverseMeasurement>,
        candidates: List<ReverseCandidate> = emptyList(),
    ): ReverseWizardResult {
        val byKey = measurements.associateBy { it.key }
        val next = nextBestMeasurement(definition, byKey)
        val warnings = definition.warnings + wornWarnings(measurements)

        if (definition.stopConditions.contains("requires_standard_table")) {
            return ReverseWizardResult.RequiresStandardTable(
                wizardId = definition.id,
                reasonRu = "Для мастера ${definition.titleRu} требуется импорт лицензированной стандартной таблицы; подстановка типовых рядов запрещена.",
                nextBestMeasurement = next,
                warnings = warnings,
            )
        }

        val providedCandidates = if (definition.id == "cylindrical_gear" && candidates.isEmpty()) {
            gearFormulaCandidates(byKey)
        } else {
            candidates
        }

        if (providedCandidates.isEmpty()) {
            return ReverseWizardResult.Scored(definition.id, emptyList(), listOf(ReverseContradiction("candidates", "Нет кандидатов для сравнения")), next, warnings)
        }

        val scored = providedCandidates.map { candidate -> score(definition, byKey, candidate) }.sortedByDescending { it.confidence }
        return ReverseWizardResult.Scored(
            wizardId = definition.id,
            candidates = scored,
            contradictions = contradictions(scored),
            nextBestMeasurement = next,
            warnings = warnings,
        )
    }

    private fun score(definition: ReverseWizardDefinition, measurements: Map<String, ReverseMeasurement>, candidate: ReverseCandidate): ScoredReverseCandidate {
        var weighted = 0.0
        var total = 0.0
        val reasons = mutableListOf<String>()
        definition.measurements.forEach { spec ->
            val measurement = measurements[spec.key] ?: return@forEach
            total += spec.weight
            val numericCandidate = candidate.values[spec.key]
            val textCandidate = candidate.textValues[spec.key]
            val contribution = when {
                measurement.value != null && numericCandidate != null -> numericContribution(definition, spec, measurement, numericCandidate)
                measurement.text != null && textCandidate != null -> if (measurement.text == textCandidate) 1.0 else 0.0
                else -> 0.0
            }
            weighted += spec.weight * contribution
            reasons += "${spec.key}: ${"%.2f".format(contribution)}"
        }
        val confidence = if (total == 0.0) 0.0 else (weighted / total).coerceIn(0.0, 1.0)
        return ScoredReverseCandidate(candidate, confidence, reasons)
    }

    private fun numericContribution(definition: ReverseWizardDefinition, spec: ReverseMeasurementSpec, measurement: ReverseMeasurement, candidateValue: Double): Double {
        val tolerance = max(measurement.uncertainty, definition.scoring.defaultToleranceMm ?: 0.0).let { if (it <= 0.0) 0.001 else it }
        val error = abs(measurement.value!! - candidateValue)
        val normalized = (1.0 - error / (tolerance * 3.0)).coerceIn(0.0, 1.0)
        val wearPenalty = when {
            !measurement.worn -> 0.0
            definition.scoring.wearAdjustedFields.contains(spec.key) -> 0.0
            spec.key == "outsideDiameter" -> definition.scoring.outsideDiameterWearPenalty ?: 0.0
            else -> definition.scoring.wearPenalty ?: 0.10
        }
        val exactBonus = if (error <= tolerance) definition.scoring.exactBonus ?: 0.0 else 0.0
        return (normalized + exactBonus - wearPenalty).coerceIn(0.0, 1.0)
    }

    private fun nextBestMeasurement(definition: ReverseWizardDefinition, measurements: Map<String, ReverseMeasurement>): String? =
        definition.measurements
            .filter { it.required && measurements[it.key] == null }
            .maxByOrNull { it.weight }
            ?.key
            ?: definition.measurements.filter { measurements[it.key] == null }.maxByOrNull { it.weight }?.key

    private fun contradictions(scored: List<ScoredReverseCandidate>): List<ReverseContradiction> = when {
        scored.isEmpty() -> emptyList()
        scored.first().confidence < 0.35 -> listOf(ReverseContradiction("confidence", "Измерения противоречат доступным кандидатам: максимальная уверенность ниже порога"))
        else -> emptyList()
    }

    private fun wornWarnings(measurements: List<ReverseMeasurement>): List<String> =
        measurements.filter { it.worn }.map { "Поле ${it.key} помечено как изношенное; уверенность снижена или поле требует подтверждения." }

    private fun gearFormulaCandidates(measurements: Map<String, ReverseMeasurement>): List<ReverseCandidate> {
        val z = measurements["z"]?.value ?: return emptyList()
        val outside = measurements["outsideDiameter"]?.value ?: return emptyList()
        val module = outside / (z + 2.0)
        return listOf(
            ReverseCandidate(
                id = "gear_module_estimate",
                title = "Оценка модуля m≈${"%.3f".format(module)} по da/(z+2)",
                values = mapOf("z" to z, "outsideDiameter" to outside, "estimatedModule" to module),
            ),
        )
    }
}
