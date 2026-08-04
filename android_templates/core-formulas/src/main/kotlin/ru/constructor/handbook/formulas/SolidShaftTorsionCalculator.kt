package ru.constructor.handbook.formulas

import kotlin.math.PI

data class SolidShaftTorsionInput(val torqueNm: Double, val diameterM: Double, val lengthM: Double, val shearModulusPa: Double)
data class SolidShaftTorsionOutput(val polarMomentM4: Double, val maxShearPa: Double, val twistRad: Double)

class SolidShaftTorsionCalculator : FormulaCalculator<SolidShaftTorsionInput, SolidShaftTorsionOutput> {
    override val formulaId = "solid_shaft_torsion"
    override val formulaVersion = 1
    override fun validate(input: SolidShaftTorsionInput) = ValidationResult(buildList {
        if (input.diameterM <= 0) add(ValidationIssue("diameterM", "positive", "Диаметр должен быть больше нуля"))
        if (input.lengthM < 0) add(ValidationIssue("lengthM", "nonnegative", "Длина не может быть отрицательной"))
        if (input.shearModulusPa <= 0) add(ValidationIssue("shearModulusPa", "positive", "Модуль сдвига должен быть больше нуля"))
    })
    override fun calculate(input: SolidShaftTorsionInput): CalculationResult<SolidShaftTorsionOutput> {
        require(validate(input).isValid)
        val j = PI * Math.pow(input.diameterM, 4.0) / 32.0
        val tau = input.torqueNm * (input.diameterM / 2.0) / j
        val phi = input.torqueNm * input.lengthM / (input.shearModulusPa * j)
        return CalculationResult(SolidShaftTorsionOutput(j, tau, phi), CalculationExplanation(
            formulaId, formulaVersion, listOf("J=πd⁴/32", "τmax=T(d/2)/J", "φ=TL/(GJ)"),
            listOf("Сплошной круглый призматический вал", "Линейная упругость"), emptyList(),
            mapOf("J" to j), emptyList(), ReviewStatus.AUTOMATIC
        ))
    }
}
