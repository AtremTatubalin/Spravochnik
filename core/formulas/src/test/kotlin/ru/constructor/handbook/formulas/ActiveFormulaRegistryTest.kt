package ru.constructor.handbook.formulas

import kotlin.math.PI
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.constructor.handbook.formulaapi.CalculationResponse
import ru.constructor.handbook.formulaapi.FormulaInput
import ru.constructor.handbook.formulaapi.Quantity

class ActiveFormulaRegistryTest {
    @Test fun implementsRequestedFormulaIdsWithSeedVersions() {
        val ids = setOf("mass_rectangular_prism", "mass_solid_cylinder", "mass_hollow_cylinder", "power_torque_speed", "linear_thermal_expansion", "liquid_volume_by_density", "axial_bar", "solid_shaft_torsion", "hollow_shaft_torsion", "beam_ss_center_point", "beam_ss_udl", "beam_cantilever_end", "beam_cantilever_udl", "bearing_basic_life", "spur_gear_basic", "helical_gear_basic", "gear_forces", "key_preliminary", "spline_bearing_preliminary", "bolt_tension_preliminary", "bolt_direct_shear_preliminary", "fillet_weld_direct_shear", "compression_spring_basic", "belt_speed", "pressure_force_area", "flow_velocity_area", "thread_pitch_tpi", "bolt_circle_coordinates", "truncated_cone_development")
        ids.forEach { id -> assertEquals(1, ActiveFormulaRegistry.find(id)?.version) }
    }

    @Test fun computesSolidMassAndThermalExpansion() {
        assertClose(61.26105674500097, success("mass_solid_cylinder", "d" to q(0.1, "m"), "L" to q(1.0, "m"), "rho" to q(7800.0, "kg/m3"))["m"]!!.value)
        assertClose(0.0012, success("linear_thermal_expansion", "alpha" to q(12e-6, "1/K"), "L" to q(2.0, "m"), "deltaT" to q(50.0, "K"))["deltaL"]!!.value)
        assertClose(0.95, success("liquid_volume_by_density", "V1" to q(1.0, "m3"), "rho1" to q(950.0, "kg/m3"), "rho2" to q(1000.0, "kg/m3"))["V2"]!!.value)
    }

    @Test fun computesStrengthAndBeamCases() {
        assertClose(50e6, success("axial_bar", "F" to q(10_000.0, "N"), "A" to q(2e-4, "m2"), "L" to q(1.0, "m"), "E" to q(200e9, "Pa"))["sigma"]!!.value)
        assertClose(500.0, success("beam_ss_center_point", "P" to q(1000.0, "N"), "L" to q(2.0, "m"), "E" to q(200e9, "Pa"), "I" to q(1e-6, "m4"))["Mmax"]!!.value)
        assertClose(1000.0, success("beam_cantilever_end", "P" to q(500.0, "N"), "L" to q(2.0, "m"), "E" to q(200e9, "Pa"), "I" to q(1e-6, "m4"))["Mmax"]!!.value)
    }

    @Test fun computesShaftBearingGearAndDevelopment() {
        assertClose(4_074_366.54315252, success("solid_shaft_torsion", "T" to q(100.0, "N*m"), "d" to q(0.05, "m"), "L" to q(1.0, "m"), "G" to q(80e9, "Pa"))["tauMax"]!!.value)
        assertClose(64.0, success("bearing_basic_life", "C" to q(20_000.0, "N"), "P" to q(5_000.0, "N"), "p" to q(3.0, "-"), "n" to q(1000.0, "rpm"))["L10"]!!.value)
        assertClose(40.0, success("spur_gear_basic", "m" to q(2.0, "mm"), "z1" to q(20.0, "-"), "z2" to q(40.0, "-"))["d1"]!!.value)
        assertClose(4000.0, success("gear_forces", "T" to q(100.0, "N*m"), "d" to q(0.05, "m"), "alpha_t" to q(0.0, "rad"), "beta" to q(0.0, "rad"))["Ft"]!!.value)
        assertClose(1.5707963267948966, success("belt_speed", "d" to q(0.1, "m"), "n" to q(300.0, "rpm"))["v"]!!.value)
        assertClose(2000.0, success("pressure_force_area", "p" to q(1_000_000.0, "Pa"), "A" to q(0.002, "m2"))["F"]!!.value)
        assertClose(0.03, success("flow_velocity_area", "v" to q(3.0, "m/s"), "A" to q(0.01, "m2"))["Q"]!!.value)
        assertClose(20.0, success("thread_pitch_tpi", "P_mm" to q(1.27, "mm"))["TPI"]!!.value)
        assertEquals(4, success("bolt_circle_coordinates", "D" to q(100.0, "mm"), "N" to q(2.0, "-"), "theta0" to q(0.0, "rad")).filterKeys { it.startsWith("x") || it.startsWith("y") }.size)
        assertTrue(success("truncated_cone_development", "D1" to q(100.0, "mm"), "D2" to q(50.0, "mm"), "s" to q(80.0, "mm"))["angle"]!!.value > 0.0)
    }

    @Test fun computesPreliminaryOnlyCalculatorsAndFlagsEngineerReview() {
        val key = response("key_preliminary", "T" to q(100.0, "N*m"), "d" to q(0.05, "m"), "b" to q(0.01, "m"), "hEff" to q(0.005, "m"), "l" to q(0.04, "m"))
        assertEquals("requires_engineer_review", key.reviewStatus)
        assertTrue(key.requiresEngineerReview)
        assertTrue(key.limitations.isNotEmpty())
        assertClose(10_000_000.0, key.output.values["tau"]!!.value)
        assertClose(20_000_000.0, key.output.values["sigmaBearing"]!!.value)

        assertClose(3_333_333.3333333335, success("spline_bearing_preliminary", "T" to q(100.0, "N*m"), "dm" to q(0.05, "m"), "zEff" to q(10.0, "-"), "h" to q(0.003, "m"), "l" to q(0.04, "m"))["p"]!!.value)
        assertClose(50_000_000.0, success("bolt_tension_preliminary", "F" to q(10_000.0, "N"), "As" to q(2e-4, "m2"))["sigma"]!!.value)
        assertClose(25_000_000.0, success("bolt_direct_shear_preliminary", "F" to q(10_000.0, "N"), "n" to q(2.0, "-"), "A_shear" to q(2e-4, "m2"))["tau"]!!.value)
        assertClose(35_360_678.925035365, success("fillet_weld_direct_shear", "F" to q(10_000.0, "N"), "k" to q(0.005, "m"), "L" to q(0.08, "m"))["tau"]!!.value)
        val spring = success("compression_spring_basic", "G" to q(80e9, "Pa"), "d" to q(0.005, "m"), "D" to q(0.05, "m"), "n" to q(8.0, "-"), "F" to q(100.0, "N"))
        assertClose(6250.0, spring["k"]!!.value)
        assertClose(0.016, spring["delta"]!!.value)
        assertTrue(spring["tau"]!!.value > 0.0)
    }

    @Test fun validatesDimensionsAndRequiresTables() {
        val invalid = ActiveFormulaRegistry.find("mass_solid_cylinder")!!.calculate(FormulaInput(mapOf("d" to q(0.1, "mm"), "L" to q(1.0, "m"), "rho" to q(7800.0, "kg/m3"))))
        assertTrue(invalid is CalculationResponse.Invalid)
        val table = ActiveFormulaRegistry.find("bearing_equivalent_dynamic")!!.calculate(FormulaInput(emptyMap()))
        assertTrue(table is CalculationResponse.RequiresStandardTable)
    }

    private fun success(id: String, vararg values: Pair<String, Quantity>): Map<String, Quantity> {
        val result = response(id, *values)
        return result.output.values + result.intermediateValues
    }

    private fun response(id: String, vararg values: Pair<String, Quantity>) =
        (ActiveFormulaRegistry.find(id)!!.calculate(FormulaInput(values.toMap())) as CalculationResponse.Success).result
    private fun q(value: Double, unit: String) = Quantity(value, unit)
    private fun assertClose(expected: Double, actual: Double) = assertEquals(expected, actual, kotlin.math.max(1e-6, kotlin.math.abs(expected) * 1e-4))
}
