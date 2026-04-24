package com.pohnpawit.jodhor.data.local.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE photos ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0")
        db.execSQL("UPDATE photos SET sortOrder = id")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE dorms ADD COLUMN securityDeposit INTEGER")
        db.execSQL("ALTER TABLE dorms ADD COLUMN advancePayment INTEGER")
        db.execSQL("ALTER TABLE dorms ADD COLUMN contractYears INTEGER")
    }
}
