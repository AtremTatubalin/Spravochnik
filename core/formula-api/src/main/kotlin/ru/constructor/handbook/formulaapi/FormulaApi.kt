package ru.constructor.handbook.formulaapi

public data class Quantity(val value: Double, val unit: String)
public data class FormulaInput(val values: Map<String, Quantity>) {
    public operator fun get(key: String): Quantity = values[key] ?: error("missing input: $key")
}

public data class FormulaOutput(val values: Map<String, Quantity>)
public data class FormulaResult(
    val formulaId: String,
    val formulaVersion: Int,
    val dataVersions: List<String>,
    val intermediateValues: Map<String, Quantity>,
    val output: FormulaOutput,
    val assumptions: List<String>,
    val warnings: List<String>,
    val sourceReferences: List<String>,
    val reviewStatus: String,
)

public data class FormulaError(val key: String, val messageRu: String)
public sealed interface CalculationResponse {
    public data class Success(val result: FormulaResult) : CalculationResponse
    public data class Invalid(val formulaId: String, val errors: List<FormulaError>) : CalculationResponse
    public data class RequiresStandardTable(val formulaId: String, val sourceReferences: List<String>, val warnings: List<String>) : CalculationResponse
}

public interface FormulaDefinition {
    public val id: String
    public val version: Int
    public val inputUnits: Map<String, String>
    public val outputUnits: Map<String, String>
    public val assumptions: List<String>
    public val warnings: List<String>
    public val sourceReferences: List<String>
    public val reviewStatus: String
    public fun calculate(input: FormulaInput): CalculationResponse
}

public interface FormulaRegistry {
    public fun find(id: String, version: Int? = null): FormulaDefinition?
    public fun all(): List<FormulaDefinition>
}
