package ru.constructor.handbook.report

public object ReportJsonExporter {
    public fun export(report: StructuredReport): String = buildString {
        append('{')
        field("id", report.id); comma()
        field("title", report.title); comma()
        field("createdAt", report.createdAt); comma()
        array("datasetVersions", report.datasetVersions); comma()
        field("reviewStatus", report.reviewStatus); comma()
        append("\"sections\":[")
        report.sections.forEachIndexed { index, section ->
            if (index > 0) comma()
            sectionJson(section)
        }
        append("]}")
    }

    private fun StringBuilder.sectionJson(section: ReportSection) {
        append('{')
        field("title", section.title); comma()
        quantities("inputs", section.inputs); comma()
        append("\"formulaVersions\":[")
        section.formulaVersions.forEachIndexed { index, ref ->
            if (index > 0) comma()
            append("{\"formulaId\":\"${escape(ref.formulaId)}\",\"version\":${ref.version}}")
        }
        append(']'); comma()
        quantities("intermediateValues", section.intermediateValues); comma()
        quantities("outputs", section.outputs); comma()
        array("assumptions", section.assumptions); comma()
        array("warnings", section.warnings); comma()
        append("\"sources\":[")
        section.sources.forEachIndexed { index, source ->
            if (index > 0) comma()
            append("{\"sourceId\":\"${escape(source.sourceId)}\",\"locator\":")
            source.locator?.let { append("\"${escape(it)}\"") } ?: append("null")
            append('}')
        }
        append("]}")
    }

    private fun StringBuilder.quantities(name: String, values: List<ReportQuantity>) {
        append("\"$name\":[")
        values.forEachIndexed { index, quantity ->
            if (index > 0) comma()
            append("{\"key\":\"${escape(quantity.key)}\",\"value\":\"${escape(quantity.value)}\",\"unit\":\"${escape(quantity.unit)}\"}")
        }
        append(']')
    }

    private fun StringBuilder.field(name: String, value: String) { append("\"$name\":\"${escape(value)}\"") }
    private fun StringBuilder.array(name: String, values: List<String>) { append("\"$name\":[${values.joinToString(",") { "\"${escape(it)}\"" }}]") }
    private fun StringBuilder.comma() { append(',') }
    private fun escape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
}

public object ReportCsvExporter {
    public fun export(report: StructuredReport): String = buildString {
        appendLine("section,kind,key,value,unit")
        report.sections.forEach { section ->
            rows(section.title, "input", section.inputs)
            rows(section.title, "intermediate", section.intermediateValues)
            rows(section.title, "output", section.outputs)
            section.formulaVersions.forEach { appendLine(csv(section.title, "formula_version", it.formulaId, it.version.toString(), "-")) }
            section.assumptions.forEach { appendLine(csv(section.title, "assumption", "assumption", it, "-")) }
            section.warnings.forEach { appendLine(csv(section.title, "warning", "warning", it, "-")) }
            section.sources.forEach { appendLine(csv(section.title, "source", it.sourceId, it.locator.orEmpty(), "-")) }
        }
        report.datasetVersions.forEach { appendLine(csv("report", "dataset_version", "dataset", it, "-")) }
        appendLine(csv("report", "review_status", "reviewStatus", report.reviewStatus, "-"))
    }

    private fun StringBuilder.rows(section: String, kind: String, quantities: List<ReportQuantity>) {
        quantities.forEach { appendLine(csv(section, kind, it.key, it.value, it.unit)) }
    }

    private fun csv(vararg values: String): String = values.joinToString(",") { value -> "\"${value.replace("\"", "\"\"")}" + "\"" }
}

public object PdfReportRenderer {
    public fun render(report: StructuredReport): ByteArray {
        val text = buildString {
            appendLine(report.title)
            appendLine("reviewStatus: ${report.reviewStatus}")
            appendLine("datasetVersions: ${report.datasetVersions.joinToString()}")
            report.sections.forEach { section ->
                appendLine(section.title)
                appendLine("inputs: ${section.inputs.joinToString { "${it.key}=${it.value} ${it.unit}" }}")
                appendLine("formulaVersions: ${section.formulaVersions.joinToString { "${it.formulaId}:v${it.version}" }}")
                appendLine("intermediate: ${section.intermediateValues.joinToString { "${it.key}=${it.value} ${it.unit}" }}")
                appendLine("outputs: ${section.outputs.joinToString { "${it.key}=${it.value} ${it.unit}" }}")
                appendLine("assumptions: ${section.assumptions.joinToString()}")
                appendLine("warnings: ${section.warnings.joinToString()}")
                appendLine("sources: ${section.sources.joinToString { it.sourceId + (it.locator?.let { loc -> ":$loc" } ?: "") }}")
            }
        }.replace("(", "\\(").replace(")", "\\)")
        val stream = "BT /F1 10 Tf 50 780 Td (${text.take(3500).replace("\n", ") Tj T* (")}) Tj ET"
        val objects = listOf(
            "1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj",
            "2 0 obj << /Type /Pages /Kids [3 0 R] /Count 1 >> endobj",
            "3 0 obj << /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >> endobj",
            "4 0 obj << /Type /Font /Subtype /Type1 /BaseFont /Helvetica >> endobj",
            "5 0 obj << /Length ${stream.length} >> stream\n$stream\nendstream endobj",
        )
        return buildString {
            appendLine("%PDF-1.4")
            objects.forEach { appendLine(it) }
            appendLine("trailer << /Root 1 0 R >>")
            appendLine("%%EOF")
        }.toByteArray(Charsets.UTF_8)
    }
}
