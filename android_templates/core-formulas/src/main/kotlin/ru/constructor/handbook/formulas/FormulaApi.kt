package ru.constructor.handbook.formulas

data class ValidationIssue(val field: String?, val code: String, val messageRu: String)
data class ValidationResult(val issues: List<ValidationIssue>) {
    val isValid: Boolean get() = issues.isEmpty()
    companion object { val Valid = ValidationResult(emptyList()) }
}

enum class ReviewStatus { AUTOMATIC, REQUIRES_ENGINEER_REVIEW, REQUIRES_STANDARD_TABLE, BLOCKED }

data class CalculationExplanation(
    val formulaId: String,
    val formulaVersion: Int,
    val expressions: List<String>,
    val assumptions: List<String>,
    val warnings: List<String>,
    val intermediate: Map<String, Double>,
    val sourceIds: List<String>,
    val reviewStatus: ReviewStatus
)

data class CalculationResult<O>(val output: O, val explanation: CalculationExplanation)

interface FormulaCalculator<I, O> {
    val formulaId: String
    val formulaVersion: Int
    fun validate(input: I): ValidationResult
    fun calculate(input: I): CalculationResult<O>
}
