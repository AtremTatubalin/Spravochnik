package ru.constructor.handbook.formulas

import kotlin.math.pow

data class BearingLifeInput(val dynamicRatingN: Double, val equivalentLoadN: Double, val exponent: Double, val speedRpm: Double)
data class BearingLifeOutput(val lifeMillionRevolutions: Double, val lifeHours: Double)

class BearingLifeCalculator : FormulaCalculator<BearingLifeInput, BearingLifeOutput> {
    override val formulaId = "bearing_basic_life"
    override val formulaVersion = 1

    override fun validate(input: BearingLifeInput): ValidationResult {
        val issues = buildList {
            if (input.dynamicRatingN <= 0) add(ValidationIssue("dynamicRatingN", "positive", "C должно быть больше нуля"))
            if (input.equivalentLoadN <= 0) add(ValidationIssue("equivalentLoadN", "positive", "P должно быть больше нуля"))
            if (input.exponent <= 0) add(ValidationIssue("exponent", "positive", "Показатель степени должен быть больше нуля"))
            if (input.speedRpm <= 0) add(ValidationIssue("speedRpm", "positive", "Частота должна быть больше нуля"))
        }
        return ValidationResult(issues)
    }

    override fun calculate(input: BearingLifeInput): CalculationResult<BearingLifeOutput> {
        require(validate(input).isValid)
        val ratio = input.dynamicRatingN / input.equivalentLoadN
        val l10 = ratio.pow(input.exponent)
        val l10h = 1_000_000.0 * l10 / (60.0 * input.speedRpm)
        return CalculationResult(
            BearingLifeOutput(l10, l10h),
            CalculationExplanation(
                formulaId, formulaVersion,
                listOf("L10=(C/P)^p", "L10h=10^6*L10/(60*n)"),
                listOf("Постоянная эквивалентная нагрузка и скорость"),
                listOf("Базовый L10 не равен скорректированному ресурсу конкретного производителя"),
                mapOf("C_over_P" to ratio),
                listOf("skf.bearing_life"),
                ReviewStatus.AUTOMATIC
            )
        )
    }
}
