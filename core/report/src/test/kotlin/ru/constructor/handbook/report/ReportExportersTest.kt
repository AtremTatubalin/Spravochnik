package ru.constructor.handbook.report

import org.junit.Assert.assertTrue
import org.junit.Test

class ReportExportersTest {
    private val report = StructuredReport(
        id = "r1",
        title = "Расчёт вала",
        createdAt = "2026-08-05T00:00:00Z",
        datasetVersions = listOf("formulas:0.1.0", "materials:0.1.0"),
        reviewStatus = "requires_engineer_review",
        sections = listOf(
            ReportSection(
                title = "Кручение",
                inputs = listOf(ReportQuantity("T", "100", "N*m"), ReportQuantity("d", "0.05", "m")),
                formulaVersions = listOf(FormulaVersionRef("solid_shaft_torsion", 1)),
                intermediateValues = listOf(ReportQuantity("J", "6.13e-7", "m4")),
                outputs = listOf(ReportQuantity("tauMax", "4074366", "Pa")),
                assumptions = listOf("линейная упругость"),
                warnings = listOf("требуется проверка инженером"),
                sources = listOf(ReportSourceRef("formulas.seed", "data/seeds/formulas.json")),
            ),
        ),
    )

    @Test fun exportsJsonCsvAndPdfWithTraceability() {
        val json = ReportJsonExporter.export(report)
        val csv = ReportCsvExporter.export(report)
        val pdf = PdfReportRenderer.render(report).toString(Charsets.UTF_8)

        assertTrue(json.contains("solid_shaft_torsion"))
        assertTrue(json.contains("formulaVersions"))
        assertTrue(csv.contains("formula_version"))
        assertTrue(csv.contains("dataset_version"))
        assertTrue(pdf.startsWith("%PDF-1.4"))
        assertTrue(pdf.contains("reviewStatus"))
        assertTrue(pdf.contains("datasetVersions"))
    }
}
