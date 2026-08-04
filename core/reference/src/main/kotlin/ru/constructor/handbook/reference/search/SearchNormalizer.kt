package ru.constructor.handbook.reference.search

import java.text.Normalizer
import java.util.Locale

public object SearchNormalizer {
    private val separators = Regex("[\\s_\\-–—./]+")
    private val nonSearchCharacters = Regex("[^a-zа-я0-9]+")

    public fun normalize(value: String): String = Normalizer.normalize(value, Normalizer.Form.NFKC)
        .lowercase(Locale.ROOT)
        .replace('ё', 'е')
        .replace("kh", "х")
        .replace('x', 'х')
        .replace(separators, "")
        .replace(nonSearchCharacters, "")
}
