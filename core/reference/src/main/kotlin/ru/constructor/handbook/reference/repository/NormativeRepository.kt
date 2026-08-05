package ru.constructor.handbook.reference.repository

import kotlinx.coroutines.flow.Flow
import ru.constructor.handbook.reference.model.Source
import ru.constructor.handbook.reference.model.Standard
import ru.constructor.handbook.reference.model.StandardChange

public interface NormativeRepository {
    public fun observeSources(): Flow<List<Source>>
    public fun observeStandards(): Flow<List<Standard>>
    public fun observeChanges(standardId: String): Flow<List<StandardChange>>
    public suspend fun searchStandards(query: String): List<Standard>
}
