
package com.devarag.forge.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseProvider {

    @Volatile
    private var INSTANCE: ForgeDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL("""
                CREATE TABLE IF NOT EXISTS absences (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    date INTEGER NOT NULL,
                    periodNumbersCsv TEXT NOT NULL,
                    reason TEXT NOT NULL,
                    notes TEXT NOT NULL,
                    createdAt INTEGER NOT NULL
                )
            """)

            db.execSQL("""
                CREATE TABLE IF NOT EXISTS duty_leaves (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    eventName TEXT NOT NULL,
                    startDate INTEGER NOT NULL,
                    endDate INTEGER NOT NULL,
                    hoursCoveredCsv TEXT NOT NULL,
                    reason TEXT NOT NULL,
                    approvalStatus TEXT NOT NULL,
                    notes TEXT NOT NULL,
                    createdAt INTEGER NOT NULL
                )
            """)
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS notes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    title TEXT NOT NULL,
                    content TEXT NOT NULL,
                    category TEXT NOT NULL,
                    isPinned INTEGER NOT NULL,
                    createdAt INTEGER NOT NULL,
                    updatedAt INTEGER NOT NULL
                )
            """)
        }
    }

    fun getDatabase(context: Context): ForgeDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ForgeDatabase::class.java,
                "forge_database"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()

            INSTANCE = instance
            instance
        }
    }
}
