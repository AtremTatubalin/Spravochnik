package ru.constructor.handbook.reverse

import kotlinx.serialization.json.Json

public object ReverseWizardCatalog {
    private val json = Json { ignoreUnknownKeys = true }

    public fun parse(jsonText: String): List<ReverseWizardDefinition> = json.decodeFromString(jsonText)

    public fun find(jsonText: String, id: String): ReverseWizardDefinition? = parse(jsonText).firstOrNull { it.id == id }
}
