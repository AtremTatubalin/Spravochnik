package ru.constructor.handbook.reference.catalog

public enum class ReferenceCardStatus { Verified, Draft, FutureReplacement, Expired, Placeholder }

public data class ReferenceCardModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val status: ReferenceCardStatus,
    val sourceId: String,
    val datasetVersion: String,
    val details: List<String> = emptyList(),
)

public data class MaterialConditionCardModel(
    val id: String,
    val materialDesignation: String,
    val conditionLabel: String,
    val verificationStatus: ReferenceCardStatus,
    val sourceId: String,
    val datasetVersion: String,
    val properties: List<String>,
)

public object SeedReferenceCatalog {
    public const val packageVersion: String = "0.1.0"
    public const val verifiedAsOf: String = "2026-08-04"

    public val standards: List<ReferenceCardModel> = listOf(
        ReferenceCardModel("gost_1139_80", "ГОСТ 1139-80", "Активный стандарт · проверено $verifiedAsOf", ReferenceCardStatus.Verified, "rst.gost1139", packageVersion, listOf("Изменения и поправки требуют официального импорта таблиц")),
        ReferenceCardModel("gost_8509_2026", "ГОСТ 8509-2026", "Будущая замена · статус переключается по effective_from", ReferenceCardStatus.FutureReplacement, "rst.gost8509.2026", packageVersion, listOf("Не применять как активный до даты вступления")),
        ReferenceCardModel("gost_8509_93", "ГОСТ 8509-93", "Заменяемый/истекающий стандарт", ReferenceCardStatus.Expired, "rst.gost8509.93", packageVersion, listOf("Показывать рядом с будущей заменой")),
    )

    public val materials: List<ReferenceCardModel> = listOf(
        ReferenceCardModel("steel_st3sp", "Ст3сп", "Черновая структура без свойств", ReferenceCardStatus.Draft, "materials.json", packageVersion, listOf("Числовые свойства отсутствуют; условия материала не импортированы")),
        ReferenceCardModel("steel_45", "45", "Черновая структура без свойств", ReferenceCardStatus.Draft, "materials.json", packageVersion, listOf("Не показываются механические свойства вне состояния материала")),
    )

    public val materialConditions: List<MaterialConditionCardModel> = emptyList()

    public val bearings: List<ReferenceCardModel> = listOf(
        ReferenceCardModel("bearing_6005", "6005", "Проверенная выборка · размеры из каталога", ReferenceCardStatus.Verified, "skf.catalog.rolling", packageVersion, listOf("d=25 мм · D=47 мм · B=12 мм", "Рейтинги не показываются, если поле пустое")),
        ReferenceCardModel("bearing_6206_rz", "6206-RZ", "Вариант RZ · проверенная выборка", ReferenceCardStatus.Verified, "skf.catalog.rolling", packageVersion, listOf("d=30 мм · D=62 мм · B=16 мм", "Вариант: RZ")),
    )

    public val profiles: List<ReferenceCardModel> = listOf(
        ReferenceCardModel("equal_angle", "Уголок равнополочный", "Таблица размеров требует лицензированного источника", ReferenceCardStatus.Draft, "profile_families.json", packageVersion, listOf("Связан с ГОСТ 8509-93 и ГОСТ 8509-2026")),
        ReferenceCardModel("parallel_flange_ibeam", "Двутавр с параллельными гранями полок", "Нужна замена источника", ReferenceCardStatus.Placeholder, "profile_families.json", packageVersion, listOf("Размерные таблицы не встроены")),
    )

    public val fitsPlaceholders: List<ReferenceCardModel> = listOf(ReferenceCardModel("fits_placeholder", "Посадки", "Нужны официальные таблицы допусков", ReferenceCardStatus.Placeholder, "requires_standard_table", packageVersion))
    public val threadsPlaceholders: List<ReferenceCardModel> = listOf(ReferenceCardModel("threads_placeholder", "Резьбы", "Нужны официальные таблицы профилей и шагов", ReferenceCardStatus.Placeholder, "requires_standard_table", packageVersion))
    public val fastenersPlaceholders: List<ReferenceCardModel> = listOf(ReferenceCardModel("fasteners_placeholder", "Крепёж", "Нужны официальные таблицы исполнений", ReferenceCardStatus.Placeholder, "requires_standard_table", packageVersion))
}
