package ru.constructor.handbook.formulas

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan
import ru.constructor.handbook.formulaapi.*

public object ActiveFormulaRegistry : FormulaRegistry {
    private val formulas = listOf(
        formula("mass_rectangular_prism", mapOf("a" to "m", "b" to "m", "h" to "m", "rho" to "kg/m3"), mapOf("V" to "m3", "m" to "kg")) { v -> mapOf("V" to v.getValue("a") * v.getValue("b") * v.getValue("h"), "m" to v.getValue("rho") * v.getValue("a") * v.getValue("b") * v.getValue("h")) },
        formula("mass_solid_cylinder", mapOf("d" to "m", "L" to "m", "rho" to "kg/m3"), mapOf("A" to "m2", "V" to "m3", "m" to "kg")) { v -> val a = PI * v.getValue("d").pow(2) / 4; val vol = a * v.getValue("L"); mapOf("A" to a, "V" to vol, "m" to v.getValue("rho") * vol) },
        formula("mass_hollow_cylinder", mapOf("D" to "m", "d" to "m", "L" to "m", "rho" to "kg/m3"), mapOf("m" to "kg"), listOf("D>d")) { v -> mapOf("m" to v.getValue("rho") * PI * (v.getValue("D").pow(2) - v.getValue("d").pow(2)) / 4 * v.getValue("L")) },
        formula("power_torque_speed", mapOf("P_kW" to "kW", "n_rpm" to "rpm"), mapOf("T" to "N*m"), listOf("стационарное вращение без учёта КПД")) { v -> mapOf("T" to 9550.0 * v.getValue("P_kW") / v.getValue("n_rpm")) },
        formula("linear_thermal_expansion", mapOf("alpha" to "1/K", "L" to "m", "deltaT" to "K"), mapOf("deltaL" to "m", "L2" to "m"), listOf("alpha постоянен в диапазоне температуры")) { v -> val d = v.getValue("alpha") * v.getValue("L") * v.getValue("deltaT"); mapOf("deltaL" to d, "L2" to v.getValue("L") + d) },
        formula("liquid_volume_by_density", mapOf("V1" to "m3", "rho1" to "kg/m3", "rho2" to "kg/m3"), mapOf("V2" to "m3", "deltaV" to "m3"), listOf("замкнутая масса без утечки"), listOf("Плотности должны поступать из проверенной температурной таблицы")) { v -> val mass = v.getValue("rho1") * v.getValue("V1"); val v2 = mass / v.getValue("rho2"); mapOf("m" to mass, "V2" to v2, "deltaV" to v2 - v.getValue("V1")) },
        formula("axial_bar", mapOf("F" to "N", "A" to "m2", "L" to "m", "E" to "Pa"), mapOf("sigma" to "Pa", "deltaL" to "m"), listOf("линейная упругость", "центральное растяжение")) { v -> mapOf("sigma" to v.getValue("F") / v.getValue("A"), "deltaL" to v.getValue("F") * v.getValue("L") / (v.getValue("E") * v.getValue("A"))) },
        formula("solid_shaft_torsion", mapOf("T" to "N*m", "d" to "m", "L" to "m", "G" to "Pa"), mapOf("J" to "m4", "tauMax" to "Pa", "phi" to "rad"), listOf("круглый призматический вал", "линейная упругость")) { v -> val j = PI * v.getValue("d").pow(4) / 32; mapOf("J" to j, "tauMax" to 16 * v.getValue("T") / (PI * v.getValue("d").pow(3)), "phi" to v.getValue("T") * v.getValue("L") / (v.getValue("G") * j)) },
        formula("hollow_shaft_torsion", mapOf("T" to "N*m", "D" to "m", "d" to "m", "L" to "m", "G" to "Pa"), mapOf("tauMax" to "Pa", "phi" to "rad"), listOf("D>d")) { v -> val j = PI * (v.getValue("D").pow(4) - v.getValue("d").pow(4)) / 32; mapOf("J" to j, "tauMax" to v.getValue("T") * (v.getValue("D") / 2) / j, "phi" to v.getValue("T") * v.getValue("L") / (v.getValue("G") * j)) },
        formula("beam_ss_center_point", mapOf("P" to "N", "L" to "m", "E" to "Pa", "I" to "m4"), mapOf("Mmax" to "N*m", "fmax" to "m"), listOf("Эйлер–Бернулли", "малые прогибы")) { v -> mapOf("R1" to v.getValue("P") / 2, "R2" to v.getValue("P") / 2, "Mmax" to v.getValue("P") * v.getValue("L") / 4, "fmax" to v.getValue("P") * v.getValue("L").pow(3) / (48 * v.getValue("E") * v.getValue("I"))) },
        formula("beam_ss_udl", mapOf("q" to "N/m", "L" to "m", "E" to "Pa", "I" to "m4"), mapOf("Mmax" to "N*m", "fmax" to "m"), listOf("постоянные E и I")) { v -> mapOf("R1" to v.getValue("q") * v.getValue("L") / 2, "R2" to v.getValue("q") * v.getValue("L") / 2, "Mmax" to v.getValue("q") * v.getValue("L").pow(2) / 8, "fmax" to 5 * v.getValue("q") * v.getValue("L").pow(4) / (384 * v.getValue("E") * v.getValue("I"))) },
        formula("beam_cantilever_end", mapOf("P" to "N", "L" to "m", "E" to "Pa", "I" to "m4"), mapOf("Mmax" to "N*m", "fend" to "m")) { v -> mapOf("Mmax" to v.getValue("P") * v.getValue("L"), "fend" to v.getValue("P") * v.getValue("L").pow(3) / (3 * v.getValue("E") * v.getValue("I"))) },
        formula("beam_cantilever_udl", mapOf("q" to "N/m", "L" to "m", "E" to "Pa", "I" to "m4"), mapOf("Mmax" to "N*m", "fend" to "m")) { v -> mapOf("Mmax" to v.getValue("q") * v.getValue("L").pow(2) / 2, "fend" to v.getValue("q") * v.getValue("L").pow(4) / (8 * v.getValue("E") * v.getValue("I"))) },
        formula("bearing_basic_life", mapOf("C" to "N", "P" to "N", "p" to "-", "n" to "rpm"), mapOf("L10" to "million_rev", "L10h" to "h"), listOf("постоянная нагрузка и скорость", "базовая надёжность метода L10"), listOf("Не заменяет скорректированную долговечность производителя")) { v -> val l10 = (v.getValue("C") / v.getValue("P")).pow(v.getValue("p")); mapOf("L10" to l10, "L10h" to 1_000_000.0 * l10 / (60 * v.getValue("n"))) },
        formula("spur_gear_basic", mapOf("m" to "mm", "z1" to "-", "z2" to "-"), mapOf("d1" to "mm", "d2" to "mm", "a" to "mm", "i" to "-"), listOf("стандартный исходный контур", "угол 20°", "нулевое смещение")) { v -> val d1 = v.getValue("m") * v.getValue("z1"); val d2 = v.getValue("m") * v.getValue("z2"); mapOf("d1" to d1, "d2" to d2, "a" to (d1 + d2) / 2, "i" to v.getValue("z2") / v.getValue("z1")) },
        formula("helical_gear_basic", mapOf("mn" to "mm", "beta" to "rad", "z1" to "-", "z2" to "-"), mapOf("mt" to "mm", "d1" to "mm", "d2" to "mm", "a" to "mm"), listOf("параллельные оси", "нулевое смещение")) { v -> val mt = v.getValue("mn") / cos(v.getValue("beta")); val d1 = mt * v.getValue("z1"); val d2 = mt * v.getValue("z2"); mapOf("mt" to mt, "d1" to d1, "d2" to d2, "a" to (d1 + d2) / 2) },
        formula("gear_forces", mapOf("T" to "N*m", "d" to "m", "alpha_t" to "rad", "beta" to "rad"), mapOf("Ft" to "N", "Fr" to "N", "Fa" to "N"), listOf("идеализированное зацепление без динамического коэффициента")) { v -> val ft = 2 * v.getValue("T") / v.getValue("d"); mapOf("Ft" to ft, "Fr" to ft * tan(v.getValue("alpha_t")), "Fa" to ft * tan(v.getValue("beta"))) },
        formula("key_preliminary", mapOf("T" to "N*m", "d" to "m", "b" to "m", "hEff" to "m", "l" to "m"), mapOf("tau" to "Pa", "sigmaBearing" to "Pa"), listOf("равномерная нагрузка"), listOf("Эффективная высота и допускаемые напряжения зависят от стандарта и конструкции", "Предварительный результат требует проверки инженером"), "requires_engineer_review") { v -> val ft = 2 * v.getValue("T") / v.getValue("d"); mapOf("Ft" to ft, "tau" to ft / (v.getValue("b") * v.getValue("l")), "sigmaBearing" to ft / (v.getValue("hEff") * v.getValue("l"))) },
        formula("spline_bearing_preliminary", mapOf("T" to "N*m", "dm" to "m", "zEff" to "-", "h" to "m", "l" to "m"), mapOf("p" to "Pa"), listOf("равномерность учитывается zEff"), listOf("zEff, h и допускаемое давление должны определяться выбранным методом/стандартом", "Предварительный результат требует проверки инженером"), "requires_engineer_review") { v -> val ft = 2 * v.getValue("T") / v.getValue("dm"); mapOf("Ft" to ft, "p" to ft / (v.getValue("zEff") * v.getValue("h") * v.getValue("l"))) },
        formula("bolt_tension_preliminary", mapOf("F" to "N", "As" to "m2"), mapOf("sigma" to "Pa"), listOf("центральная статическая нагрузка"), listOf("Не учитывает предварительную затяжку, податливость стыка и усталость", "Предварительный результат требует проверки инженером"), "requires_engineer_review") { v -> mapOf("sigma" to v.getValue("F") / v.getValue("As")) },
        formula("bolt_direct_shear_preliminary", mapOf("F" to "N", "n" to "-", "A_shear" to "m2"), mapOf("tau" to "Pa"), listOf("равномерное распределение"), listOf("Не учитывает смятие, эксцентриситет и проскальзывание", "Предварительный результат требует проверки инженером"), "requires_engineer_review") { v -> mapOf("tau" to v.getValue("F") / (v.getValue("n") * v.getValue("A_shear"))) },
        formula("fillet_weld_direct_shear", mapOf("F" to "N", "k" to "m", "L" to "m"), mapOf("tau" to "Pa"), listOf("равномерный прямой срез"), listOf("Не применять к сложной группе швов без нормативного расчёта", "Предварительный результат требует проверки инженером"), "requires_engineer_review") { v -> val a = 0.707 * v.getValue("k"); val aw = a * v.getValue("L"); mapOf("a" to a, "Aw" to aw, "tau" to v.getValue("F") / aw) },
        formula("compression_spring_basic", mapOf("G" to "Pa", "d" to "m", "D" to "m", "n" to "-", "F" to "N"), mapOf("k" to "N/m", "delta" to "m", "tau" to "Pa"), listOf("круглая проволока", "линейная упругость"), listOf("Проверка устойчивости, соударения витков и усталости отдельна", "Предварительный результат требует проверки инженером"), "requires_engineer_review") { v -> val stiffness = v.getValue("G") * v.getValue("d").pow(4) / (8 * v.getValue("D").pow(3) * v.getValue("n")); val c = v.getValue("D") / v.getValue("d"); val kw = (4 * c - 1) / (4 * c - 4) + 0.615 / c; mapOf("k" to stiffness, "delta" to v.getValue("F") / stiffness, "C" to c, "Kw" to kw, "tau" to kw * 8 * v.getValue("F") * v.getValue("D") / (PI * v.getValue("d").pow(3))) },
        formula("belt_speed", mapOf("d" to "m", "n" to "rpm"), mapOf("v" to "m/s")) { v -> mapOf("v" to PI * v.getValue("d") * v.getValue("n") / 60) },
        formula("pressure_force_area", mapOf("p" to "Pa", "A" to "m2"), mapOf("F" to "N")) { v -> mapOf("F" to v.getValue("p") * v.getValue("A")) },
        formula("flow_velocity_area", mapOf("v" to "m/s", "A" to "m2"), mapOf("Q" to "m3/s")) { v -> mapOf("Q" to v.getValue("v") * v.getValue("A")) },
        formula("thread_pitch_tpi", mapOf("P_mm" to "mm"), mapOf("TPI" to "1/in")) { v -> mapOf("TPI" to 25.4 / v.getValue("P_mm")) },
        formula("bolt_circle_coordinates", mapOf("D" to "mm", "N" to "-", "theta0" to "rad"), mapOf("r" to "mm")) { v -> val r = v.getValue("D") / 2; (0 until v.getValue("N").toInt()).flatMap { i -> val a = v.getValue("theta0") + 2 * PI * i / v.getValue("N"); listOf("x$i" to r * cos(a), "y$i" to r * sin(a)) }.toMap() + ("r" to r) },
        formula("truncated_cone_development", mapOf("D1" to "mm", "D2" to "mm", "s" to "mm"), mapOf("R1" to "mm", "R2" to "mm", "angle" to "rad"), listOf("развёртка боковой поверхности усечённого конуса")) { v -> val r1 = v.getValue("D1") / 2; val r2 = v.getValue("D2") / 2; val big = v.getValue("s") * r1 / (r1 - r2); val small = v.getValue("s") * r2 / (r1 - r2); mapOf("R1" to big, "R2" to small, "angle" to 2 * PI * r1 / big) },
        requiresTable("bearing_equivalent_dynamic", mapOf("Fr" to "N", "Fa" to "N", "X" to "-", "Y" to "-"), listOf("skf.catalog.rolling"), listOf("Нельзя использовать универсальные X/Y; брать из каталога конкретного производителя")),
    )

    override fun find(id: String, version: Int?): FormulaDefinition? = formulas.firstOrNull { it.id == id && (version == null || it.version == version) }
    override fun all(): List<FormulaDefinition> = formulas
}

private fun formula(id: String, inputs: Map<String, String>, outputs: Map<String, String>, assumptions: List<String> = emptyList(), warnings: List<String> = emptyList(), reviewStatus: String = "automatic", block: (Map<String, Double>) -> Map<String, Double>): FormulaDefinition = KotlinFormula(id, 1, inputs, outputs, assumptions, warnings, emptyList(), reviewStatus, block)
private fun requiresTable(id: String, inputs: Map<String, String>, sources: List<String>, warnings: List<String>): FormulaDefinition = KotlinFormula(id, 1, inputs, emptyMap(), emptyList(), warnings, sources, "requires_standard_table") { emptyMap() }

private class KotlinFormula(
    override val id: String,
    override val version: Int,
    override val inputUnits: Map<String, String>,
    override val outputUnits: Map<String, String>,
    override val assumptions: List<String>,
    override val warnings: List<String>,
    override val sourceReferences: List<String>,
    override val reviewStatus: String,
    val block: (Map<String, Double>) -> Map<String, Double>,
) : FormulaDefinition {
    override fun calculate(input: FormulaInput): CalculationResponse {
        if (reviewStatus == "requires_standard_table") return CalculationResponse.RequiresStandardTable(id, sourceReferences, warnings)
        val errors = validate(input)
        if (errors.isNotEmpty()) return CalculationResponse.Invalid(id, errors)
        val numeric = input.values.mapValues { it.value.value }
        val raw = block(numeric)
        val quantities = raw.mapValues { (key, value) -> Quantity(value, outputUnits[key] ?: inferredUnit(key)) }
        return CalculationResponse.Success(
            FormulaResult(id, version, listOf("formulas:0.1.0"), quantities - outputUnits.keys, FormulaOutput(quantities.filterKeys(outputUnits::containsKey)), assumptions, warnings, sourceReferences, reviewStatus),
        )
    }

    private fun validate(input: FormulaInput): List<FormulaError> = buildList {
        inputUnits.forEach { (key, unit) ->
            val q = input.values[key]
            if (q == null) add(FormulaError(key, "обязательный вход отсутствует"))
            else {
                if (q.unit != unit) add(FormulaError(key, "ожидалась единица $unit, получено ${q.unit}"))
                if (!q.value.isFinite()) add(FormulaError(key, "значение должно быть конечным"))
                if (key != "deltaT" && key != "theta0" && key != "beta" && key != "alpha_t" && q.value <= 0.0) add(FormulaError(key, "значение должно быть больше нуля"))
            }
        }
        if ((id == "mass_hollow_cylinder" || id == "hollow_shaft_torsion") && input.values["D"] != null && input.values["d"] != null && input.values.getValue("D").value <= input.values.getValue("d").value) add(FormulaError("D", "наружный диаметр должен быть больше внутреннего"))
        if (id == "truncated_cone_development" && input.values["D1"] != null && input.values["D2"] != null && input.values.getValue("D1").value <= input.values.getValue("D2").value) add(FormulaError("D1", "D1 должен быть больше D2 для текущей развёртки"))
        if (id == "bolt_circle_coordinates" && input.values["N"] != null && input.values.getValue("N").value.toInt().toDouble() != input.values.getValue("N").value) add(FormulaError("N", "число отверстий должно быть целым"))
        if (id == "compression_spring_basic" && input.values["D"] != null && input.values["d"] != null && input.values.getValue("D").value <= input.values.getValue("d").value) add(FormulaError("D", "средний диаметр пружины должен быть больше диаметра проволоки"))
    }
}
private fun inferredUnit(key: String): String = when {
    key.startsWith("x") || key.startsWith("y") -> "mm"
    key == "J" -> "m4"
    key.startsWith("R") -> "N"
    else -> "-"
}

