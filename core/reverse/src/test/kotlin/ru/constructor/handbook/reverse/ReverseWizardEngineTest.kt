package ru.constructor.handbook.reverse

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReverseWizardEngineTest {
    private val definitions = ReverseWizardCatalog.parse(File("../../data/seeds/reverse_wizards.json").readText())

    @Test fun parsesSeedWizardDefinitions() {
        assertEquals(listOf("bearing_by_dimensions", "metric_thread", "straight_spline", "involute_spline", "cylindrical_gear"), definitions.map { it.id })
    }

    @Test fun scoresBearingCandidatesWithUncertaintyWearAndNextMeasurement() {
        val definition = definitions.first { it.id == "bearing_by_dimensions" }
        val result = ReverseWizardEngine.evaluate(
            definition = definition,
            measurements = listOf(
                ReverseMeasurement("d", 25.02, unit = "mm", uncertainty = 0.05),
                ReverseMeasurement("D", 52.00, unit = "mm", uncertainty = 0.05),
                ReverseMeasurement("B", 14.80, unit = "mm", uncertainty = 0.20, worn = true),
            ),
            candidates = listOf(
                ReverseCandidate("bearing_6005", "6005", mapOf("d" to 25.0, "D" to 47.0, "B" to 12.0), sourceId = "skf.catalog.rolling"),
                ReverseCandidate("bearing_6205", "6205", mapOf("d" to 25.0, "D" to 52.0, "B" to 15.0), sourceId = "skf.catalog.rolling"),
            ),
        ) as ReverseWizardResult.Scored

        assertEquals("type", result.nextBestMeasurement)
        assertEquals("bearing_6205", result.candidates.first().candidate.id)
        assertTrue(result.candidates.first().confidence > 0.70)
        assertTrue(result.warnings.any { it.contains("изношенное") })
    }

    @Test fun detectsContradictionsWhenCandidateConfidenceIsLow() {
        val definition = definitions.first { it.id == "bearing_by_dimensions" }
        val result = ReverseWizardEngine.evaluate(
            definition,
            listOf(
                ReverseMeasurement("d", 25.0, unit = "mm", uncertainty = 0.05),
                ReverseMeasurement("D", 80.0, unit = "mm", uncertainty = 0.05),
                ReverseMeasurement("B", 30.0, unit = "mm", uncertainty = 0.05),
            ),
            listOf(ReverseCandidate("bearing_6205", "6205", mapOf("d" to 25.0, "D" to 52.0, "B" to 15.0))),
        ) as ReverseWizardResult.Scored

        assertTrue(result.contradictions.isNotEmpty())
    }

    @Test fun splineWizardsStopAtRequiresStandardTable() {
        listOf("straight_spline", "involute_spline").forEach { id ->
            val result = ReverseWizardEngine.evaluate(definitions.first { it.id == id }, emptyList())
            assertTrue(result is ReverseWizardResult.RequiresStandardTable)
            assertTrue((result as ReverseWizardResult.RequiresStandardTable).reasonRu.contains("требуется импорт"))
        }
    }

    @Test fun cylindricalGearProducesFormulaCandidateAndNextMeasurement() {
        val definition = definitions.first { it.id == "cylindrical_gear" }
        val result = ReverseWizardEngine.evaluate(
            definition,
            listOf(
                ReverseMeasurement("z", 20.0, unit = "count"),
                ReverseMeasurement("outsideDiameter", 44.0, unit = "mm", uncertainty = 0.1, worn = true),
            ),
        ) as ReverseWizardResult.Scored

        assertEquals("baseTangentLength", result.nextBestMeasurement)
        assertEquals("gear_module_estimate", result.candidates.first().candidate.id)
        assertTrue(result.candidates.first().candidate.values["estimatedModule"]!! > 0.0)
    }
}
