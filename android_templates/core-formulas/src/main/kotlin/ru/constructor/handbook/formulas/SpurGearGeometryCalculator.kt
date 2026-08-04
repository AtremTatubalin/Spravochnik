package ru.constructor.handbook.formulas

data class SpurGearInput(val moduleMm: Double, val teethPinion: Int, val teethGear: Int)
data class SpurGearOutput(val d1Mm: Double, val d2Mm: Double, val centerDistanceMm: Double, val ratio: Double)

class SpurGearGeometryCalculator : FormulaCalculator<SpurGearInput, SpurGearOutput> {
    override val formulaId = "spur_gear_basic"
    override val formulaVersion = 1
    override fun validate(input: SpurGearInput) = ValidationResult(buildList {
        if (input.moduleMm <= 0) add(ValidationIssue("moduleMm", "positive", "Модуль должен быть больше нуля"))
        if (input.teethPinion <= 0) add(ValidationIssue("teethPinion", "positive", "Число зубьев должно быть больше нуля"))
        if (input.teethGear <= 0) add(ValidationIssue("teethGear", "positive", "Число зубьев должно быть больше нуля"))
    })
    override fun calculate(input: SpurGearInput): CalculationResult<SpurGearOutput> {
        require(validate(input).isValid)
        val d1 = input.moduleMm * input.teethPinion
        val d2 = input.moduleMm * input.teethGear
        val a = (d1 + d2) / 2.0
        val ratio = input.teethGear.toDouble() / input.teethPinion
        return CalculationResult(SpurGearOutput(d1, d2, a, ratio), CalculationExplanation(
            formulaId, formulaVersion, listOf("d=mz", "a=(d1+d2)/2", "i=z2/z1"),
            listOf("Прямозубая пара, нулевое суммарное смещение"),
            listOf("Не является расчётом прочности зубьев"), emptyMap(), emptyList(), ReviewStatus.AUTOMATIC
        ))
    }
}
