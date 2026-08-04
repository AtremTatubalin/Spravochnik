package ru.constructor.handbook.database

import android.content.Context
import androidx.room.Room

public object HandbookDatabaseFactory {
    public fun create(context: Context, name: String = "handbook.db"): HandbookDatabase =
        Room.databaseBuilder(context.applicationContext, HandbookDatabase::class.java, name)
            .addCallback(HandbookDatabase.CREATE_SEARCH_INDEX)
            .addMigrations(HandbookDatabase.MIGRATION_1_2, HandbookDatabase.MIGRATION_2_3)
            .build()
}
