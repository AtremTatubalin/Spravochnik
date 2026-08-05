package ru.constructor.handbook.report

public data class StructuredReport(
    val id: String,
    val title: String,
    val createdAt: String,
    val datasetVersions: List<String>,
    val sections: List<ReportSection>,
    val reviewStatus: String,
)

public data class ReportSection(
    val title: String,
    val inputs: List<ReportQuantity> = emptyList(),
    val formulaVersions: List<FormulaVersionRef> = emptyList(),
    val intermediateValues: List<ReportQuantity> = emptyList(),
    val outputs: List<ReportQuantity> = emptyList(),
    val assumptions: List<String> = emptyList(),
    val warnings: List<String> = emptyList(),
    val sources: List<ReportSourceRef> = emptyList(),
)

public data class ReportQuantity(val key: String, val value: String, val unit: String)
public data class FormulaVersionRef(val formulaId: String, val version: Int)
public data class ReportSourceRef(val sourceId: String, val locator: String? = null)
